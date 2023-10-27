package av.kochekov.playlistmaker.library.domain

import av.kochekov.playlistmaker.library.domain.db.FavoriteTrackInteractor
import av.kochekov.playlistmaker.search.data.model.Track
import av.kochekov.playlistmaker.search.domain.model.TrackInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoriteTrackInteractorImpl(
    private val repository: FavoriteTrackRepository
) : FavoriteTrackInteractor {
    override fun getTracks(): Flow<List<Track>> {
        return repository.getTracks()
    }

    override fun getTrackIds(): Flow<List<Int>> {
        return repository.getTrackIds()
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

}