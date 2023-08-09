package av.kochekov.playlistmaker.network_client

import av.kochekov.playlistmaker.player.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}