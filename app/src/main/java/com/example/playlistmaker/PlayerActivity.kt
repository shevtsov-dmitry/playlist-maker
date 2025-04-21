package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity : AppCompatActivity() {

    private lateinit var playPauseButton: ImageButton
    private lateinit var likeButton: ImageButton
    private lateinit var timerTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var mediaPlayer: MediaPlayer

    private var isPlaying = false
    private val handler = Handler(Looper.getMainLooper())

    private var songPath: String? = null
    private var songTitle: String = "Unknown"
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // UI references
        playPauseButton = findViewById(R.id.play_pause_button)
        likeButton = findViewById(R.id.like_button)
        timerTextView = findViewById(R.id.timer)
        titleTextView = findViewById(R.id.song_title)

        // Data from intent
        songTitle = intent.getStringExtra("song_title") ?: "Unknown"
        songPath = intent.getStringExtra("song_path")

        titleTextView.text = songTitle

        if (songPath == null) {
            Toast.makeText(this, "Путь к песне не найден", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        sharedPreferences = getSharedPreferences("favorites", Context.MODE_PRIVATE)

        // Initialize player
        mediaPlayer = MediaPlayer().apply {
            setDataSource(songPath)
            prepare()
        }

        // Like button icon state
        updateLikeButton()

        playPauseButton.setOnClickListener {
            if (isPlaying) pauseMusic() else playMusic()
        }

        likeButton.setOnClickListener {
            toggleFavorite()
        }
    }

    private fun playMusic() {
        mediaPlayer.start()
        isPlaying = true
        playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
        updateTimer()
    }

    private fun pauseMusic() {
        mediaPlayer.pause()
        isPlaying = false
        playPauseButton.setImageResource(android.R.drawable.ic_media_play)
    }

    private fun updateTimer() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                val seconds = mediaPlayer.currentPosition / 1000
                val minutes = seconds / 60
                val remaining = seconds % 60
                timerTextView.text = String.format("%d:%02d", minutes, remaining)
                if (isPlaying) handler.postDelayed(this, 500)
            }
        }, 0)
    }

    private fun toggleFavorite() {
        val favorites = sharedPreferences.getStringSet("favorite_songs", mutableSetOf()) ?: mutableSetOf()
        val editor = sharedPreferences.edit()

        if (favorites.contains(songPath)) {
            favorites.remove(songPath)
            Toast.makeText(this, "Удалено из избранного", Toast.LENGTH_SHORT).show()
        } else {
            favorites.add(songPath)
            Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
        }

        editor.putStringSet("favorite_songs", favorites)
        editor.apply()
        updateLikeButton()
    }

    private fun updateLikeButton() {
        val favorites = sharedPreferences.getStringSet("favorite_songs", emptySet()) ?: emptySet()
        val isLiked = favorites.contains(songPath)
        likeButton.setImageResource(
            if (isLiked) android.R.drawable.btn_star_big_on
            else android.R.drawable.btn_star_big_off
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }
}
