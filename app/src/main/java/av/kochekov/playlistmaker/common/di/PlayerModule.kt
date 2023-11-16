package av.kochekov.playlistmaker.player.di

import av.kochekov.playlistmaker.player.data.MediaPlayerImpl
import av.kochekov.playlistmaker.player.domain.MediaPlayerInteractor
import av.kochekov.playlistmaker.player.domain.MediaPlayerInteractorImpl
import av.kochekov.playlistmaker.player.presentation.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    single<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(
            player = MediaPlayerImpl()
        )
    }

    viewModel<PlayerViewModel> {
        PlayerViewModel(
            mediaPlayerInteractor = get(),
            favoriteTrackInteractor = get(),
            playlistInteractor = get()
        )
    }
}