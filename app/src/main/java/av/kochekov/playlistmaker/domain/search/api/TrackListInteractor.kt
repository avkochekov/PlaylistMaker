package av.kochekov.playlistmaker.domain.search.api

import av.kochekov.playlistmaker.domain.search.model.Track

interface TrackListInteractor {
    fun searchTracks(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundMovies: List<Track>?)
    }
}