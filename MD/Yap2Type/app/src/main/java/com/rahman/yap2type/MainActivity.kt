package com.rahman.yap2type

import android.animation.ObjectAnimator
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fab: FloatingActionButton
    private lateinit var parentLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        fab = findViewById(R.id.fab)
        parentLayout = findViewById(R.id.fragment_container)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.home -> HomeFragment()
                R.id.settings -> SettingsFragment()
                else -> throw IllegalArgumentException("Unexpected item ID")
            }
            replaceFragment(selectedFragment)
            true
        }

        fab.setOnClickListener {
            showPopupWindow()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }

    private fun showPopupWindow() {
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.popup_source, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        popupWindow.elevation = 10.0f
        popupWindow.isFocusable = true
        popupWindow.update()

        // Dimming effect
        val dimBackground = ColorDrawable(resources.getColor(android.R.color.black, null))
        dimBackground.alpha = 0
        parentLayout.foreground = dimBackground
        animateDim(parentLayout, 150, 128)

        popupWindow.setOnDismissListener {
            animateDim(parentLayout, 150, 0)
        }

        popupWindow.showAtLocation(parentLayout, android.view.Gravity.CENTER, 0, 0)

        // Handle popup item clicks if needed
        popupView.findViewById<View>(R.id.tvVideoRecorder).setOnClickListener {
            // Handle click
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
    }

    private fun animateDim(view: View, duration: Long, alpha: Int) {
        val animator = ObjectAnimator.ofInt(view.foreground, "alpha", alpha)
        animator.duration = duration
        animator.start()
    }
}
