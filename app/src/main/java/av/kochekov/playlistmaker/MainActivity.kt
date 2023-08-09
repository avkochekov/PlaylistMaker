package av.kochekov.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import av.kochekov.playlistmaker.library.activity.LibraryActivity
import av.kochekov.playlistmaker.search.activity.SearchActivity
import av.kochekov.playlistmaker.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.menu_search).setOnClickListener{startActivity(Intent(this, SearchActivity::class.java))}
        findViewById<Button>(R.id.menu_library).setOnClickListener{startActivity(Intent(this, LibraryActivity::class.java))}
        findViewById<Button>(R.id.menu_settings).setOnClickListener{startActivity(Intent(this, SettingsActivity::class.java))}
    }
}