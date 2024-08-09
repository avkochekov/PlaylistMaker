package av.kochekov.playlistmaker.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.player.presentation.PlayerFragment
import av.kochekov.playlistmaker.search.presentation.composeUI.SearchPage
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(){
    private val viewModel by viewModel<SearchViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SearchPage(
                    onTrackClicked = {track ->
                        findNavController().navigate(
                            R.id.action_searchFragment_to_playerFragment,
                            PlayerFragment.createArgs(track)
                        )
                    }
                )
            }
        }
    }
}