package av.kochekov.playlistmaker.search.data.network_client.itunes

import av.kochekov.playlistmaker.search.data.model.TrackListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    companion object {
        const val apiUrl: String = "https://itunes.apple.com"
    }

    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): TrackListResponse
}