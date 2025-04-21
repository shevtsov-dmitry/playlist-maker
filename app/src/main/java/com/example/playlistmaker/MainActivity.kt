package com.example.playlistmaker

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mediaButton: Button = findViewById(R.id.btn_media)
        val settingsButton: Button = findViewById(R.id.btn_settings)
        val searchButton: Button = findViewById(R.id.btn_search)
        val shareButton: AppCompatImageButton = findViewById(R.id.share_button)


        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }


        mediaButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                checkAndRequestPermission(android.Manifest.permission.READ_MEDIA_AUDIO)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkAndRequestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                navigateToMediaActivity()
            }
        }


        searchButton.setOnClickListener {
            try {
                navigateToSearchActivity()
            } catch (e: Exception) {
                Toast.makeText(this, "Ошибка при переходе на страницу поиска", Toast.LENGTH_SHORT)
                    .show()
                e.printStackTrace()
            }
        }


        shareButton.setOnClickListener {
            shareApplication()
        }
    }

    private fun checkAndRequestPermission(permission: String) {
        if (ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            navigateToMediaActivity()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(permission), REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun navigateToMediaActivity() {
        val intent = Intent(this, MediaActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToSearchActivity() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    private fun shareApplication() {
        val shareText = "Ознакомьтесь с нашим потрясающим приложением! Группа: [Пока не выбрана]"
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        try {
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        } catch (e: Exception) {
            Toast.makeText(this, "No apps available for sharing", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                navigateToMediaActivity()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 123
    }
}