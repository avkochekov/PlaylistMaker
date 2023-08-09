package av.kochekov.playlistmaker.repository.track_list_repository.data

import av.kochekov.playlistmaker.player.dto.TrackListRequest
import av.kochekov.playlistmaker.player.dto.TrackListResponse
import av.kochekov.playlistmaker.network_client.itunes.ITunesApi
import av.kochekov.playlistmaker.network_client.NetworkClient
import av.kochekov.playlistmaker.repository.track_list_repository.domain.TrackListRepository
import av.kochekov.playlistmaker.repository.track_list_repository.models.Track
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrackListRepositoryImpl(private val networkClient: NetworkClient) :
    TrackListRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl(ITunesApi.apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

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
                    it.previewUrl)
            }
        } else {
            return null
        }
    }
}