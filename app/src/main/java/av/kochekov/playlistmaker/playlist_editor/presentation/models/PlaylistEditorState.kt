package av.kochekov.playlistmaker.playlist_editor.presentation.models

import av.kochekov.playlistmaker.playlist_editor.domain.models.PlaylistModel


sealed class PlaylistEditorState{
    data class Saved(
        val data: PlaylistModel
    ) :  PlaylistEditorState()
    data class Data(
        val data: PlaylistModel
    ) :  PlaylistEditorState()
}
