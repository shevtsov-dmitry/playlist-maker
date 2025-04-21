package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences

object FavoritesManager {
    private const val PREFS_NAME = "favorites_prefs"
    private const val FAVORITES_KEY = "favorites"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun addToFavorites(context: Context, path: String) {
        val favorites = getFavorites(context).toMutableSet()
        favorites.add(path)
        getPrefs(context).edit().putStringSet(FAVORITES_KEY, favorites).apply()
    }

    fun removeFromFavorites(context: Context, path: String) {
        val favorites = getFavorites(context).toMutableSet()
        favorites.remove(path)
        getPrefs(context).edit().putStringSet(FAVORITES_KEY, favorites).apply()
    }

    fun isFavorite(context: Context, path: String): Boolean {
        return getFavorites(context).contains(path)
    }

    fun getFavorites(context: Context): Set<String> {
        return getPrefs(context).getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
    }
}
