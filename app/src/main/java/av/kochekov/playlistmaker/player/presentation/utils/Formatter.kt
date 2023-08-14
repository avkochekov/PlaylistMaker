package av.kochekov.playlistmaker.player.presentation.utils

import android.icu.text.SimpleDateFormat
import java.util.*

object Formatter {
    fun timeToText(time: Int): String? {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }
}