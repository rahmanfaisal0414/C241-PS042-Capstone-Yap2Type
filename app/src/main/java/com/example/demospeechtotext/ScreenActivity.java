package com.example.demospeechtotext;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ScreenActivity extends AppCompatActivity {

    TextView resultTextView;
    TextView viewSummary;
    ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_summary);

        viewSummary = findViewById(R.id.result_text_view_forsummary);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);
        loadingProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));

        Button backButton = findViewById(R.id.backback);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke MainActivity
                finish();
            }
        });

        // Ambil data dari DataHolder jika ada
        String transcription = DataHolder.getInstance().getTranscription();
        if (transcription != null) {
            // Kirim data ke method untuk diolah
            receiveTextFromMainActivity(transcription);
        }
    }

    // Metode untuk menerima dan memproses data dari MainActivity
    public void receiveTextFromMainActivity(String transcription) {
        // Proses transkripsi di sini
        showAlert2(transcription);
        // Misalnya, lakukan proses kirim ke backend dengan AsyncTask seperti sebelumnya
        sendTextToBackend(transcription);
    }

    private void sendTextToBackend(String transcription) {
        String cloudFunctionUrl = "https://summarizeapi-7c7o3mtyua-et.a.run.app/summarize";
        String dataToSend = "{\"text\": \"" + transcription + "\"}";

        loadingProgressBar.setVisibility(View.VISIBLE);

        try {
            new SendDataTask().execute(cloudFunctionUrl, dataToSend);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error: " + e.getMessage());
            loadingProgressBar.setVisibility(View.GONE);
        }
    }

    private void processResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String summary = jsonResponse.getString("summary");

            // Update UI with the summary text
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    viewSummary.setText(summary);
                    showAlert(summary);
                }
            });

            // Log the response without curly braces
            Log.d("SendDataTask", "Response from server: " + summary);

        } catch (JSONException e) {
            e.printStackTrace();
            showAlert("Error parsing response");
        }

        loadingProgressBar.setVisibility(View.GONE);
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Server Response")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nothing to do here
                    }
                })
                .show();
    }

    private void showAlert2(String message) {
        new AlertDialog.Builder(this)
                .setTitle("data yang akan di transkipsi")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nothing to do here
                    }
                })
                .show();
    }

    private class SendDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String dataToSend = params[1];
            StringBuilder response = new StringBuilder();
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = dataToSend.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            response.append(line).append("\n");
                        }
                    }
                } else {
                    response.append("Error: ").append(conn.getResponseCode()).append(" ").append(conn.getResponseMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
                response.append("Error: ").append(e.getMessage());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            // Process the response after doInBackground finishes
            processResponse(response);
        }
    }
}
