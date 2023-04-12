package av.kochekov.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var searchButton = findViewById<Button>(R.id.menu_search)
        var libraryButton = findViewById<Button>(R.id.menu_library)
        var settingsButton = findViewById<Button>(R.id.menu_settings)

        searchButton.setOnClickListener{startActivity(Intent(this, SearchActivity::class.java))}
        libraryButton.setOnClickListener{startActivity(Intent(this, LibraryActivity::class.java))}
        settingsButton.setOnClickListener{startActivity(Intent(this, SettingsActivity::class.java))}
    }
}