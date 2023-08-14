package av.kochekov.playlistmaker.settings.domain
import android.content.Context
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.settings.data.ExternalNavigator
import av.kochekov.playlistmaker.settings.domain.models.EmailData

class SharingInteractorImpl(private val context: Context) : SharingInteractor {
    private val externalNavigator = ExternalNavigator(context)
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.settings_shareAddress)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = context.getString(R.string.settings_mailAddress),
            subject = context.getString(R.string.settings_mailSubject),
            text = context.getString(R.string.settings_mailText)
        )
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.settings_licenseAddress)
    }
}