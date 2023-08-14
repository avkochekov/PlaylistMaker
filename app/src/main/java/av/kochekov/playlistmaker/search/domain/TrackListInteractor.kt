package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.search.data.model.Track

interface TrackListInteractor {
    fun searchTracks(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>?)
    }
}