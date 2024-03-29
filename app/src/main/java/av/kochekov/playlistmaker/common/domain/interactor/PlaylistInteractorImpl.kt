package av.kochekov.playlistmaker.common.domain.interactor

import av.kochekov.playlistmaker.common.domain.PlaylistRepository
import av.kochekov.playlistmaker.favorite_tracks.data.utils.Mapper as TrackMapper
import av.kochekov.playlistmaker.playlist_editor.domain.PlaylistInteractor
import av.kochekov.playlistmaker.playlist_editor.data.utils.Mapper as PlaylistMapper
import av.kochekov.playlistmaker.images.domain.ImagesRepository
import av.kochekov.playlistmaker.playlist_editor.domain.models.PlaylistModel
import av.kochekov.playlistmaker.search.domain.model.TrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import java.util.*

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val imagesRepository: ImagesRepository
) : PlaylistInteractor {
    override fun savePlaylist(data: PlaylistModel): Flow<PlaylistModel?> = flow {
        if (data.uuid.isEmpty()) {
            data.uuid = UUID.randomUUID().toString()
            data.artwork = imagesRepository.saveImage(
                path = data.artwork,
                name = data.uuid
            ).toString()
        }

        if (playlistRepository.containsPlaylists(data.uuid).single()) {
            playlistRepository.updatePlaylist(PlaylistMapper.fromModel(data)).collect {
                emit(it?.let { PlaylistMapper.toModel(it) } ?: null)
            }
        } else {
            playlistRepository.addPlaylist(PlaylistMapper.fromModel(data)).collect {
                emit(it?.let { PlaylistMapper.toModel(it) } ?: null)
            }
        }
    }

    override fun getPlaylist(uuid: String): Flow<PlaylistModel?> = flow {
        playlistRepository.getPlaylist(uuid).collect {
            emit(it?.let { PlaylistMapper.toModel(it) } ?: null)
        }
    }

    override fun getPlaylists(): Flow<List<PlaylistModel>> = flow {
        playlistRepository.getPlaylists().collect { list ->
            emit(list.map { data -> PlaylistMapper.toModel(data) })
        }
    }

    override fun addToPlaylist(udi: String, track: TrackModel): Flow<List<PlaylistModel>> = flow {
        playlistRepository.addTrack(udi, TrackMapper.fromModel(track))
        playlistRepository.getPlaylists().collect { list ->
            emit(list.map { data -> PlaylistMapper.toModel(data) })
        }
    }

    override fun contains(udi: String, id: Int): Flow<Boolean> {
        return playlistRepository.containsTrack(udi, id)
    }
}