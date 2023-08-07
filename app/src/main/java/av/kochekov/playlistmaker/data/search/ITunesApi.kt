package av.kochekov.playlistmaker.data.search

import av.kochekov.playlistmaker.data.dto.TrackListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    companion object{
        const val apiUrl: String = "https://itunes.apple.com"
    }

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackListResponse>
}