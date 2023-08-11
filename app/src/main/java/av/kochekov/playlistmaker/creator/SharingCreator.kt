package av.kochekov.playlistmaker.creator

import android.content.Context
import av.kochekov.playlistmaker.settings.domain.SharingInteractorImpl
import av.kochekov.playlistmaker.settings.domain.SharingInteractor

object SharingCreator {
    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(context)
    }
}