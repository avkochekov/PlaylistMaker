package av.kochekov.playlistmaker.search.data

import av.kochekov.playlistmaker.search.domain.TrackListRepository
import av.kochekov.playlistmaker.search.data.model.TrackListRequest
import av.kochekov.playlistmaker.search.data.model.TrackListResponse
import av.kochekov.playlistmaker.search.data.network_client.NetworkClient
import av.kochekov.playlistmaker.search.data.model.Track

class TrackListRepositoryImpl(private val networkClient: NetworkClient) :
    TrackListRepository {

    override fun search(query: String): List<Track>? {
        val response = networkClient.doRequest(TrackListRequest(query))
        if (response.resultCode == 200) {
            return (response as TrackListResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        } else {
            return null
        }
    }
}