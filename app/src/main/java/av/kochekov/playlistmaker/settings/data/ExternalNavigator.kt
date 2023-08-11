package av.kochekov.playlistmaker.settings.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import av.kochekov.playlistmaker.settings.domain.models.EmailData

class ExternalNavigator (private val context: Context) {
    fun shareLink(link: String){
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
            context.startActivity(this)
        }
    }

    fun openLink(link: String){
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(link)
            context.startActivity(this)
        }
    }

    fun openEmail(emailData: EmailData){
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(emailData.email))
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
            context.startActivity(this)
        }
    }
}