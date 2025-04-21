package com.example.playlistmaker

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SearchActivity : AppCompatActivity() {

    private lateinit var songListView: ListView
    private lateinit var noSongsMessage: TextView
    private lateinit var adapter: SongCursorAdapter
    private var currentCursor: Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        songListView = findViewById(R.id.songListView)
        noSongsMessage = findViewById(R.id.no_songs_message)
        val searchView: SearchView = findViewById(R.id.searchView)

        adapter = SongCursorAdapter(this, null)
        songListView.adapter = adapter

        songListView.setOnItemClickListener { parent, _, position, _ ->
            val cursor = parent.getItemAtPosition(position) as Cursor
            val songTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
            val songPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

            val intent = Intent(this, PlayerActivity::class.java).apply {
                putExtra("song_title", songTitle)
                putExtra("song_path", songPath)
            }
            startActivity(intent)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadSongsFromStorage(newText)
                return true
            }
        })
    }

    private fun loadSongsFromStorage(filter: String?) {
        currentCursor?.close()
        val musicUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.DATA} LIKE ? AND ${MediaStore.Audio.Media.TITLE} LIKE ?"
        val selectionArgs = arrayOf("%/Music/%", "%${filter ?: ""}%")

        val cursor = contentResolver.query(
            musicUri,
            projection,
            selection,
            selectionArgs,
            MediaStore.Audio.Media.TITLE + " ASC"
        )

        currentCursor = cursor

        if (cursor != null && cursor.count > 0) {
            noSongsMessage.visibility = View.GONE
            songListView.visibility = View.VISIBLE
            adapter.changeCursor(cursor)
        } else {
            songListView.visibility = View.GONE
            noSongsMessage.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        currentCursor?.close()
    }
}
