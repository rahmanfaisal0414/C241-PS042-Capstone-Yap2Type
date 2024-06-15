package com.rahman.yap2type

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnAddAudio: Button = view.findViewById(R.id.btnAddAudio)

        val popupView = layoutInflater.inflate(R.layout.popup_source, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val parentLayout: ConstraintLayout = view.findViewById(R.id.parentLayout)

        val dimBackground = ColorDrawable(resources.getColor(android.R.color.black, null))
        dimBackground.alpha = 0

        parentLayout.foreground = dimBackground

        popupWindow.setOnDismissListener {
            animateDim(parentLayout, 150, 0)
        }

        val showPopup = View.OnClickListener {
            animateDim(parentLayout, 150, 128)
            popupWindow.showAtLocation(parentLayout, android.view.Gravity.CENTER, 0, 0)
        }

        btnAddAudio.setOnClickListener(showPopup)

        // Handle popup item clicks if needed
        popupView.findViewById<View>(R.id.tvVideoRecorder).setOnClickListener {
            // Start AudioRecorder activity
            val intent = Intent(activity, AudioRecorderActivity::class.java)
            startActivity(intent)
            popupWindow.dismiss()
        }
        popupView.findViewById<View>(R.id.tvUpload).setOnClickListener {
            // Handle click
            popupWindow.dismiss()
        }
        popupView.findViewById<View>(R.id.tvYouTube).setOnClickListener {
            // Handle click
            popupWindow.dismiss()
        }

        return view
    }

    private fun animateDim(view: View, duration: Long, alpha: Int) {
        val animator = ObjectAnimator.ofInt(view.foreground, "alpha", alpha)
        animator.duration = duration
        animator.start()
    }
}