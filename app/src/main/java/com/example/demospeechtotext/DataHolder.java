package com.example.demospeechtotext;
public class DataHolder {
    private static final DataHolder instance = new DataHolder();
    private String transcription;

    private DataHolder() {
        // Private constructor to prevent instantiation.
    }

    public static DataHolder getInstance() {
        return instance;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }
}
