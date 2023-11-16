package av.kochekov.playlistmaker.search.domain

import av.kochekov.playlistmaker.common.data.models.Track

interface SearchHistoryRepository {

    fun setTrackList(list: List<Track>)

    fun getTrackList() : List<Track>

    fun clear();
}