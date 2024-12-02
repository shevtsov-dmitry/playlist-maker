package com.example.playlistmaker

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cursoradapter.widget.SimpleCursorAdapter

class MediaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        val songListView: ListView = findViewById(R.id.songListView)

        val songs = getSongsFromStorage()
        var songsAmount = songs?.count ?: 0

        if (songsAmount == 0) {
            // Show a message if no songs are found
            Toast.makeText(this, "Песни не найдены.", Toast.LENGTH_LONG).show()
        } else {
            // Display the songs in a ListView
            val adapter = SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1, // Simple item layout for each song
                songs,
                arrayOf(MediaStore.Audio.Media.TITLE), // Column to display
                intArrayOf(android.R.id.text1), // Which text view to use
                0
            )

            songListView.adapter = adapter
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
}
