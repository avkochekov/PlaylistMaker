package av.kochekov.playlistmaker.data.search

import av.kochekov.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}