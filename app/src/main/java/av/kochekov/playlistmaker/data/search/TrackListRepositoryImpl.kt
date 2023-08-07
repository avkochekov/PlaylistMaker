package av.kochekov.playlistmaker.data.search

import av.kochekov.playlistmaker.data.dto.TrackListRequest
import av.kochekov.playlistmaker.data.dto.TrackListResponse
import av.kochekov.playlistmaker.domain.search.api.TrackListRepositoryInterface
import av.kochekov.playlistmaker.domain.search.model.Track
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrackListRepositoryImpl(private val networkClient: NetworkClient) : TrackListRepositoryInterface {
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