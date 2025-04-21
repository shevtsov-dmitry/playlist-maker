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
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        darkModeSwitch = findViewById(R.id.switch_dark_mode)
        darkModeSwitch.isChecked = isDarkMode
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        supportButton = findViewById(R.id.support_button)
        supportButton.setOnClickListener {
            showSupportDialog()
        }

        userAgreementButton = findViewById(R.id.user_agreement_button)
        userAgreementButton.setOnClickListener {
            openUserAgreement()
        }
    }


    private fun openUserAgreement() {
        val url = "https://vivt.ru/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showSupportDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_support, null)
        val emailEditText = dialogView.findViewById<EditText>(R.id.email_input)
        val messageEditText = dialogView.findViewById<EditText>(R.id.message_input)

        val builder =
            AlertDialog.Builder(this).setTitle("Связаться с техподдержкой").setView(dialogView)
                .setPositiveButton("Отправить") { _, _ ->
                    val email = emailEditText.text.toString()
                    val message = messageEditText.text.toString()

                    sendMessageToSupport(email, message)
                }.setNegativeButton("Отменить", null)

        builder.create().show()
    }

    private fun sendMessageToSupport(email: String, message: String) {
        Toast.makeText(
            this, "Сообщение отправлено. Ждите ответа в ближайшее время.", Toast.LENGTH_LONG
        ).show()
    }
}
