package av.kochekov.playlistmaker.search.data.network_client.itunes

import av.kochekov.playlistmaker.search.data.model.Response
import av.kochekov.playlistmaker.search.data.model.TrackListRequest
import av.kochekov.playlistmaker.search.data.network_client.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunesNetworkClient(private val service: ITunesApi) : NetworkClient {

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