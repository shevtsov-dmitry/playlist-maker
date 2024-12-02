package com.example.playlistmaker

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {

    private lateinit var darkModeSwitch: Switch
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Set up the Toolbar with a back button
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable back button

        // Initialize SharedPreferences to save the mode preference
        sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)

        // Get the current mode from shared preferences
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)

        // Initialize the switch for dark mode toggle
        darkModeSwitch = findViewById(R.id.switch_dark_mode)

        // Set the initial state of the switch
        darkModeSwitch.isChecked = isDarkMode

        // Set an OnCheckedChangeListener to save the preference
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Save the dark mode preference
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()

            // Change the theme based on the switch position
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Apply the theme based on the current preference
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    // Handle back button press
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()  // Navigate back to the previous activity
        return true
    }
}
