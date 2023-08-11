package av.kochekov.playlistmaker.settings.presentation
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import av.kochekov.playlistmaker.settings.domain.SettingsInteractor
import av.kochekov.playlistmaker.settings.domain.models.ThemeSettings
import av.kochekov.playlistmaker.settings.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private var isDarkTheme = MutableLiveData<Boolean>()

    init {
        isDarkTheme.value = settingsInteractor.getThemeSettings() == ThemeSettings.DARK
        updateTheme()
    }

    fun isDarkTheme() : LiveData<Boolean>{
        return  isDarkTheme
    }

    fun shareApp(){
        sharingInteractor.shareApp()
    }

    fun openSupport(){
        sharingInteractor.openSupport()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun switchTheme(){
        settingsInteractor.updateThemeSetting(if (isDarkTheme.value == true) ThemeSettings.LIGHT else ThemeSettings.DARK)
        isDarkTheme.value = (isDarkTheme.value != true)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme.value == true) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        fun getViewModelFactory(
            sharingInteractor: SharingInteractor,
            settingsInteractor: SettingsInteractor
        ) : ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(
                        sharingInteractor = sharingInteractor,
                        settingsInteractor = settingsInteractor
                    ) as T
                }
            }
    }

    private fun updateTheme(){
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme.value == true) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}