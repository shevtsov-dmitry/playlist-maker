package com.example.playlistmaker

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Set up the Toolbar with a back button
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable back button

        // Initialize the SearchView
        val searchView: SearchView = findViewById(R.id.searchView)

        // Set up a query listener on the search view
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission (e.g., start a search or filter results)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle query text change (e.g., update search results dynamically)
                return false
            }
        })
    }

    // Handle back button press
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()  // Navigate back to the previous activity
        return true
    }
}
