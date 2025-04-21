package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity : AppCompatActivity() {

    private lateinit var playPauseButton: ImageButton
    private lateinit var timerTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playPauseButton = findViewById(R.id.play_pause_button)
        timerTextView = findViewById(R.id.timer)
        titleTextView = findViewById(R.id.song_title)

        val songTitle = intent.getStringExtra("song_title") ?: "Unknown"
        val songPath = intent.getStringExtra("song_path")

        titleTextView.text = songTitle

        if (songPath == null) {
            Toast.makeText(this, "Путь к песне не найден", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        mediaPlayer = MediaPlayer().apply {
            setDataSource(songPath)
            prepare()
        }

        playPauseButton.setOnClickListener {
            if (isPlaying) pauseMusic() else playMusic()
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

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacksAndMessages(null)
    }
}
