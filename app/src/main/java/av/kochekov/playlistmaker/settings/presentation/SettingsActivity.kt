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
import av.kochekov.playlistmaker.creator.RepositoryCreator
import av.kochekov.playlistmaker.settings.presentation.SettingsViewModel.Companion.getViewModelFactory
import av.kochekov.playlistmaker.creator.SharingCreator

@SuppressLint("RestrictedApi")
class SettingsActivity : AppCompatActivity()  {

    private lateinit var viewModel: SettingsViewModel

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel = ViewModelProvider(this, getViewModelFactory(
            sharingInteractor = SharingCreator.provideSharingInteractor(this),
            settingsInteractor = RepositoryCreator.provideSettingsInteractor(this)
        )).get(SettingsViewModel::class.java)

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