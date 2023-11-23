import av.kochekov.playlistmaker.settings.domain.SharingInteractor

import av.kochekov.playlistmaker.common.data.ExternalNavigator
import av.kochekov.playlistmaker.common.data.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
) : SharingInteractor {
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
        return externalNavigator.getAppLink()
    }

    private fun getSupportEmailData(): EmailData {
        return externalNavigator.getEmailData()
    }

    private fun getTermsLink(): String {
        return externalNavigator.getTermLink()
    }
}