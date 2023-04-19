package av.kochekov.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<Toolbar>(R.id.settings_toolbar).setNavigationOnClickListener {
            onBackPressed()
        }

        findViewById<LinearLayout>(R.id.share).setOnClickListener {
            var intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/learn/android-developer")
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.support).setOnClickListener {
            var intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("deliremail@gmail.com"))
            intent.putExtra(Intent.EXTRA_TITLE, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
            intent.putExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!")
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.license).setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            startActivity(intent)
        }
    }
}