package av.kochekov.playlistmaker.settings.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.player.presentation.PlayerViewModel
import av.kochekov.playlistmaker.settings.SettingsCreator
import av.kochekov.playlistmaker.settings.presentation.SettingsViewModel.Companion.getViewModelFactory
import av.kochekov.playlistmaker.settings.SharingCreator
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("RestrictedApi")
class SettingsActivity : AppCompatActivity()  {

    private val viewModel by viewModel<SettingsViewModel>()

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel.isDarkTheme().observe(this, Observer {
            val checked = it
            findViewById<Switch>(R.id.themeSwitcher).let {
                it.isChecked = checked
            }
        })

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressed()
        }

        findViewById<LinearLayout>(R.id.share).setOnClickListener {
            viewModel.shareApp()
        }

        findViewById<LinearLayout>(R.id.support).setOnClickListener {
            viewModel.openSupport()
        }

        findViewById<LinearLayout>(R.id.license).setOnClickListener {
            viewModel.openTerms()
        }
        findViewById<Switch>(R.id.themeSwitcher).setOnClickListener{
            viewModel.switchTheme()
        }
    }
}