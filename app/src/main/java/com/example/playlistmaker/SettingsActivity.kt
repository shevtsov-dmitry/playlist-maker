package com.example.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {

    private lateinit var darkModeSwitch: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var supportButton: AppCompatImageButton
    private lateinit var userAgreementButton: Button


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

        // Support button action
        supportButton = findViewById(R.id.support_button)
        supportButton.setOnClickListener {
            showSupportDialog()
        }

        // Set up the User Agreement button
        userAgreementButton = findViewById(R.id.user_agreement_button)
        userAgreementButton.setOnClickListener {
            openUserAgreement()
        }
    }

    // Function to open the User Agreement in the default browser
    private fun openUserAgreement() {
        val url = "https://vivt.ru/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    // Handle back button press
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()  // Navigate back to the previous activity
        return true
    }

    // Show the support dialog to enter email and message
    private fun showSupportDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_support, null)
        val emailEditText = dialogView.findViewById<EditText>(R.id.email_input)
        val messageEditText = dialogView.findViewById<EditText>(R.id.message_input)

        val builder = AlertDialog.Builder(this)
            .setTitle("Связаться с техподдержкой")
            .setView(dialogView)
            .setPositiveButton("Отправить") { _, _ ->
                val email = emailEditText.text.toString()
                val message = messageEditText.text.toString()

                sendMessageToSupport(email, message)
            }
            .setNegativeButton("Отменить", null)

        builder.create().show()
    }

    // TODO send message to server
    private fun sendMessageToSupport(email: String, message: String) {
        Toast.makeText(
            this,
            "Сообщение отправлено. Ждите ответа в ближайшее время.",
            Toast.LENGTH_LONG
        ).show()
    }
}
