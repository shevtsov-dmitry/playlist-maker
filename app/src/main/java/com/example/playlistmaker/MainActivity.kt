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

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                // Permission granted, proceed to MediaActivity
                navigateToMediaActivity()
            } else {
                // Show a message if permission is denied
                Toast.makeText(
                    this,
                    "В разрешении отказано. Не удается получить доступ к аудиофайлам.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the media button
        val mediaButton: Button = findViewById(R.id.btn_media)

        // Set an OnClickListener for the button
        mediaButton.setOnClickListener {
            // Check Android version to determine which permission to request
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // For Android 13 (API level 33) and above, use READ_MEDIA_AUDIO
                checkAndRequestPermission(android.Manifest.permission.READ_MEDIA_AUDIO)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // For Android 6.0 (API level 23) to Android 12 (API level 32), use READ_EXTERNAL_STORAGE
                checkAndRequestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                // For older versions (below Android 6.0), no runtime permission is required
                navigateToMediaActivity()
            }
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
