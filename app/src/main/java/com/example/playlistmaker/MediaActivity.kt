package com.example.playlistmaker

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cursoradapter.widget.SimpleCursorAdapter
import java.io.File

class MediaActivity : AppCompatActivity() {

    val REQUEST_CODE_PERMISSION = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val songListView: ListView = findViewById(R.id.songListView)
        val noSongsMessage: TextView = findViewById(R.id.no_songs_message)
        val openFolderButton: Button = findViewById(R.id.openFolderButton)

        val songs = getSongsFromStorage()
        val songsAmount = songs?.count ?: 0

        if (songsAmount == 0) {
            // Show the "No songs found" message
            noSongsMessage.visibility = View.VISIBLE
            openFolderButton.visibility = View.VISIBLE  // Show the open folder button
            songListView.visibility = View.GONE // Hide the ListView when no songs are found

            // Set up the button to open the songs folder
            openFolderButton.setOnClickListener {
                openSongsFolder()
            }
        } else {
            noSongsMessage.visibility = View.GONE
            // Show the ListView with songs
            val adapter = SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1, // Simple item layout for each song
                songs,
                arrayOf(MediaStore.Audio.Media.TITLE), // Column to display
                intArrayOf(android.R.id.text1), // Which text view to use
                0
            )
            songListView.adapter = adapter
            songListView.visibility = View.VISIBLE
            openFolderButton.visibility = View.GONE  // Hide the open folder button when songs are available
        }
    }

    private fun getSongsFromStorage(): Cursor? {
        // The URI to query the songs in the Music directory
        val musicUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        // Projection columns
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA
        )

        val resolver: ContentResolver = contentResolver

        return resolver.query(
            musicUri,
            projection,
            MediaStore.Audio.Media.DATA + " LIKE ?",
            arrayOf("%/Music/%"),
            MediaStore.Audio.Media.TITLE + " ASC"
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to open the folder
                openSongsFolder()
            } else {
                Toast.makeText(this, "Отказано в доступе", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun openSongsFolder() {
        val musicDirectory = "/storage/emulated/0/Music/"
        val directory = File(musicDirectory)

        if (directory.exists() && directory.isDirectory) {
            val folderUri = Uri.parse("file://$musicDirectory") // Use 'file://' URI

            // Create an intent to open the folder in a file manager
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = folderUri
                type = "resource/folder" // MIME type for folder browsing
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            try {
                startActivity(intent) // Try starting the activity to open the folder
            } catch (e: ActivityNotFoundException) {
                // Handle the case where no file manager is found
                Toast.makeText(this, "Нет доступа к менеджеру файлов.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Музыка не найдена.", Toast.LENGTH_LONG).show()
        }
    }



}
