package av.kochekov.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressed()
        }

        findViewById<LinearLayout>(R.id.share).setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_shareAddress))
                startActivity(this)
            }
        }

        findViewById<LinearLayout>(R.id.support).setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.settings_mailAddress)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_mailSubject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_mailText))
                startActivity(this)
            }
        }

        findViewById<LinearLayout>(R.id.license).setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.settings_licenseAddress))
                startActivity(this)
            }
        }
        findViewById<Switch>(R.id.themeSwitcher).let {
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            it.isChecked = currentNightMode == Configuration.UI_MODE_NIGHT_YES
            it.setOnCheckedChangeListener { switcher, checked ->
                (applicationContext as App).setIsNightMode(checked)
            }
        }
    }
}