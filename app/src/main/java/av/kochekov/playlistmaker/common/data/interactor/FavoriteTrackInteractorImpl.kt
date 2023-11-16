package av.kochekov.playlistmaker.common.data.interactor

import av.kochekov.playlistmaker.favorite.domain.FavoriteTrackRepository
import av.kochekov.playlistmaker.favorite.domain.FavoriteTrackRepositoryObserver
import av.kochekov.playlistmaker.favorite_tracks.domain.FavoriteTrackInteractor
import av.kochekov.playlistmaker.common.data.models.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(
    private val repository: FavoriteTrackRepository
) : FavoriteTrackInteractor {
    override fun getTracks(): Flow<List<Track>> {
        return repository.getTracks()
    }

    override fun getTrackIds(): Flow<List<Int>> {
        return repository.getTrackIds()
    }

    override fun getInFavorites(track: Int): Flow<Boolean> {
        return repository.containsTrack(track)
    }

    override fun addTrack(trackInfo: Track) {
        val track = Track(
            trackInfo.trackId,
            trackInfo.trackName,
            trackInfo.artistName,
            trackInfo.trackTimeMillis,
            trackInfo.artworkUrl100,
            trackInfo.collectionName,
            trackInfo.releaseDate,
            trackInfo.primaryGenreName,
            trackInfo.country,
            trackInfo.previewUrl
        )
        GlobalScope.async { repository.addTrack(track) }
    }

    override fun removeTrack(trackInfo: Track) {
        val track = Track(
            trackInfo.trackId,
            trackInfo.trackName,
            trackInfo.artistName,
            trackInfo.trackTimeMillis,
            trackInfo.artworkUrl100,
            trackInfo.collectionName,
            trackInfo.releaseDate,
            trackInfo.primaryGenreName,
            trackInfo.country,
            trackInfo.previewUrl
        )
        GlobalScope.async { repository.removeTrack(track) }
    }

    override fun observe(observer: FavoriteTrackRepositoryObserver) {
        repository.attach(observer)
    }

}