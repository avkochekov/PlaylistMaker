package av.kochekov.playlistmaker.search.data.network_client.itunes

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import av.kochekov.playlistmaker.search.data.model.Response
import av.kochekov.playlistmaker.search.data.model.TrackListRequest
import av.kochekov.playlistmaker.search.data.network_client.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ITunesNetworkClient(private val service: ITunesApi, private val context: Context) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        if (dto !is TrackListRequest) {
            return Response().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = service.search(dto.expression)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}