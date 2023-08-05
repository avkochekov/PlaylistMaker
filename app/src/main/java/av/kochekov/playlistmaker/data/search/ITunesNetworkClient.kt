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
        if (dto is TrackListRequest) {
            val resp = service.search(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }



        if (dto is TrackListRequest) {
            val resp = service.search(dto.expression).execute()
            return (resp.body() ?: Response().apply {
                resultCode = resp.code()
            }) as Response
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}