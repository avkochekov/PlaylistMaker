package av.kochekov.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    companion object{
        var apiUrl: String = "https://itunes.apple.com"
    }

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>
}