package av.kochekov.playlistmaker.repository.track_list_repository.domain

import av.kochekov.playlistmaker.repository.track_list_repository.models.Track

interface TrackListInteractor {
    fun searchTracks(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>?)
    }
}