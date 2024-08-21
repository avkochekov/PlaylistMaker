package av.kochekov.playlistmaker.library.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.library.presentation.composeUI.LibraryPage
import av.kochekov.playlistmaker.player.presentation.PlayerFragment
import av.kochekov.playlistmaker.playlist_view.presentation.PlaylistViewFragment

class LibraryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                LibraryPage(
                    onTrackClicked = {track ->
                        findNavController().navigate(
                            R.id.action_libraryFragment_to_playerFragment,
                            PlayerFragment.createArgs(track)
                        )
                    },
                    onPlaylistClicked = {playlist ->
                        findNavController().navigate(
                            R.id.action_libraryFragment_to_playlistViewFragment,
                            PlaylistViewFragment.createArgs(playlist.uuid)
                        )
                    },
                    newPlaylistClicked = {
                        findNavController().navigate(
                            R.id.action_libraryFragment_to_playlistFragment,
                            PlaylistViewFragment.createArgs()
                        )
                    }
                )
            }
        }
    }
}