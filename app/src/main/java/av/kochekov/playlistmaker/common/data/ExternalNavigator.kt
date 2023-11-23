package av.kochekov.playlistmaker.common.data

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.common.data.models.EmailData

class ExternalNavigator(private val context: Context) {
    fun shareText(text: String) {
        shareLink(text)
    }

    fun shareLink(link: String) {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
            flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(this)
        }
    }

    fun openLink(link: String) {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(link)
            flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(this)
        }
    }

    fun openEmail(emailData: EmailData) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
            flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(this)
        }
    }

    fun getAppLink(): String {
        return context.getString(R.string.settings_shareAddress)
    }

    fun getEmailData(): EmailData {
        return EmailData(
            email = context.getString(R.string.settings_mailAddress),
            subject = context.getString(R.string.settings_mailSubject),
            text = context.getString(R.string.settings_mailText)
        )
    }

    fun getTermLink(): String {
        return context.getString(R.string.settings_licenseAddress)
    }
}