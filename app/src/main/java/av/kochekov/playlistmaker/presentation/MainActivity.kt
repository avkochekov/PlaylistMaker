package av.kochekov.playlistmaker.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.presentation.search.SearchActivity
import av.kochekov.playlistmaker.presentation.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.menu_search).setOnClickListener{startActivity(Intent(this, SearchActivity::class.java))}
        findViewById<Button>(R.id.menu_library).setOnClickListener{startActivity(Intent(this, LibraryActivity::class.java))}
        findViewById<Button>(R.id.menu_settings).setOnClickListener{startActivity(Intent(this, SettingsActivity::class.java))}
    }
}