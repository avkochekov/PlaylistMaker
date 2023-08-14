package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.search.data.model.Track

interface SearchHistoryRepository {

    fun setTrackList(list: List<Track>)

    fun getTrackList() : List<Track>

    fun clear();
}