package av.kochekov.playlistmaker.common.di

import android.media.MediaPlayer
import org.koin.dsl.module

var serviceModule = module {
    factory { MediaPlayer() }
}