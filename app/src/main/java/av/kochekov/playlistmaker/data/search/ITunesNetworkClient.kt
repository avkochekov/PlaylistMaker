package av.kochekov.playlistmaker.data.search

import av.kochekov.playlistmaker.data.dto.Response
import av.kochekov.playlistmaker.data.dto.TrackListRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunesNetworkClient : NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITunesApi.apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        return if (dto is TrackListRequest) {
            try {
                val resp = service.search(dto.expression).execute()
                val body = resp.body() ?: Response()
                body.apply { resultCode = resp.code() }
            } catch (e: Exception) {
                Response().apply { resultCode = -1 }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}