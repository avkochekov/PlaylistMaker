package av.kochekov.playlistmaker.sharing.creator

import android.content.Context
import av.kochekov.playlistmaker.sharing.data.SharingInteractorImpl
import av.kochekov.playlistmaker.sharing.domain.SharingInteractor

object SharingCreator {
    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(context)
    }
}