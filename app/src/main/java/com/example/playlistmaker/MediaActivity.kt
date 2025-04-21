package com.example.playlistmaker

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cursoradapter.widget.SimpleCursorAdapter
import java.io.File

class MediaActivity : AppCompatActivity() {

    private val REQUEST_CODE_PERMISSION = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val songListView: ListView = findViewById(R.id.songListView)
        val noSongsMessage: TextView = findViewById(R.id.no_songs_message)

        songListView.setOnItemClickListener { parent, view, position, id ->
            val cursor = parent.getItemAtPosition(position) as Cursor
            val songTitle =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
            val songPath =
                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

            val intent = Intent(this, PlayerActivity::class.java).apply {
                putExtra("song_title", songTitle)
                putExtra("song_path", songPath)
            }
            startActivity(intent)
        }


        val songs = getSongsFromStorage()
        val songsAmount = songs?.count ?: 0

        if (songsAmount == 0) {

            noSongsMessage.visibility = View.VISIBLE
            songListView.visibility = View.GONE
        } else {
            noSongsMessage.visibility = View.GONE
            val adapter = SongCursorAdapter(this, songs)
            songListView.adapter = adapter
            songListView.visibility = View.VISIBLE
        }
    }

    private fun getSongsFromStorage(): Cursor? {

        val musicUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI


        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ARTIST
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                openSongsFolder()
            } else {
                Toast.makeText(this, "Отказано в доступе", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun openSongsFolder() {
        val musicDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        val directory = File(musicDirectory.path)

        if (directory.exists() && directory.isDirectory) {
            val folderUri = Uri.parse("file://$musicDirectory")
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = folderUri
                type = "resource/folder"
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {

                Toast.makeText(this, "Нет доступа к менеджеру файлов.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Музыка не найдена.", Toast.LENGTH_LONG).show()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
