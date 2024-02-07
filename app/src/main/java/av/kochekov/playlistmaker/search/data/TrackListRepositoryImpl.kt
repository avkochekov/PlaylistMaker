package av.kochekov.playlistmaker.search.data

import av.kochekov.playlistmaker.search.domain.TrackListRepository
import av.kochekov.playlistmaker.search.data.model.TrackListRequest
import av.kochekov.playlistmaker.search.data.model.TrackListResponse
import av.kochekov.playlistmaker.search.data.network_client.NetworkClient
import av.kochekov.playlistmaker.common.data.models.Track
import av.kochekov.playlistmaker.search.domain.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackListRepositoryImpl(private val networkClient: NetworkClient) :
    TrackListRepository {

    override fun search(query: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackListRequest(query))
        when (response.resultCode) {
            200 -> {
                with(response as TrackListResponse) {
                    val data = results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error(""))
            }
        }
    }
}