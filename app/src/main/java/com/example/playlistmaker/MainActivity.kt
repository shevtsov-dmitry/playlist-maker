package com.example.playlistmaker

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mediaButton: Button = findViewById(R.id.btn_media)
        val settingsButton: Button = findViewById(R.id.btn_settings)
        val searchButton: Button = findViewById(R.id.btn_search)  // Add reference to btn_search

        // Open SettingsActivity when settings button is clicked
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Check permission and navigate to MediaActivity when media button is clicked
        mediaButton.setOnClickListener {
            // Check Android version to determine which permission to request
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                checkAndRequestPermission(android.Manifest.permission.READ_MEDIA_AUDIO)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkAndRequestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                navigateToMediaActivity()
            }
        }

        // Open SearchActivity when search button is clicked
        searchButton.setOnClickListener {
            navigateToSearchActivity()
        }
    }

    private fun checkAndRequestPermission(permission: String) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, go to MediaActivity
            navigateToMediaActivity()
        } else {
            // Request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun navigateToMediaActivity() {
        // Start MediaActivity when permission is granted
        val intent = Intent(this, MediaActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToSearchActivity() {
        // Start SearchActivity when the search button is clicked
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to MediaActivity
                navigateToMediaActivity()
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 123
    }
}
