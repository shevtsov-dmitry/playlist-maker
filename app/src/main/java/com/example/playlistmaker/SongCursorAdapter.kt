package com.example.playlistmaker

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.TextView

class SongCursorAdapter(context: Context, cursor: Cursor?) :
    CursorAdapter(context, cursor, 0) {


    override fun newView(context: Context, cursor: Cursor?, parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.item_song, parent, false)
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        val titleTextView = view.findViewById<TextView>(R.id.song_title)
        val artistTextView = view.findViewById<TextView>(R.id.song_artist)
        val icon = view.findViewById<ImageView>(R.id.icon_music)

        val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
        val artist = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)) ?: "Unknown Artist"

        titleTextView.text = title
        artistTextView.text = artist
        icon.setImageResource(android.R.drawable.ic_media_play)
    }


    fun Cursor.getStringOrNull(index: Int): String? {
        return if (isNull(index)) null else getString(index)
    }


}
