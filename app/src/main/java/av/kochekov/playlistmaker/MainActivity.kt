package av.kochekov.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var searchButton = findViewById<Button>(R.id.menu_search)
        var libraryButton = findViewById<Button>(R.id.menu_library)
        var settingsButton = findViewById<Button>(R.id.menu_settings)

        // Using lambdas - v.1 =====================================================================
        searchButton.setOnClickListener{startActivity(Intent(this, SearchActivity::class.java))}
        libraryButton.setOnClickListener{startActivity(Intent(this, LibraryActivity::class.java))}
        settingsButton.setOnClickListener{startActivity(Intent(this, SettingsActivity::class.java))}

//        // Using lambdas - v.2 =====================================================================
//        var onClickListner: View.OnClickListener = View.OnClickListener { v ->
//            when (v?.id ?: -1) {
//                R.id.menu_search -> startActivity(Intent(this@MainActivity, SearchActivity::class.java))
//                R.id.menu_library -> startActivity(Intent(this@MainActivity, LibraryActivity::class.java))
//                R.id.menu_settings -> startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
//            }
//        }

//        // Using anonymous classes =================================================================
//        var onClickListner: View.OnClickListener = object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                when (v?.id ?: -1) {
//                    R.id.menu_search -> startActivity(Intent(this@MainActivity, SearchActivity::class.java))
//                    R.id.menu_library -> startActivity(Intent(this@MainActivity, LibraryActivity::class.java))
//                    R.id.menu_settings -> startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
//                }
//            }
//        }
//        searchButton.setOnClickListener(onClickListner)
//        libraryButton.setOnClickListener(onClickListner)
//        settingsButton.setOnClickListener(onClickListner)

    }
}