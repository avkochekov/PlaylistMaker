package av.kochekov.playlistmaker.search.data.network_client

import av.kochekov.playlistmaker.search.data.model.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}