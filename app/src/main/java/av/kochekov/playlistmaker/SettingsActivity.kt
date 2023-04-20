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
        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressed()
        }

        findViewById<LinearLayout>(R.id.share).setOnClickListener {
            var intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, R.string.settings_shareAddress.toString())
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.support).setOnClickListener {
            var intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(R.string.settings_mailAddress))
            intent.putExtra(Intent.EXTRA_SUBJECT, R.string.settings_mailSubject)
            intent.putExtra(Intent.EXTRA_TEXT, R.string.settings_mailText)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.license).setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(R.string.settings_licenseAddress.toString())
            startActivity(intent)
        }
    }
}