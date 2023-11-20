package av.kochekov.playlistmaker.library.presentation.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.databinding.FragmentPlaylistsBinding
import av.kochekov.playlistmaker.library.presentation.playlists.models.PlaylistState
import av.kochekov.playlistmaker.playlist_view.presentation.PlaylistViewFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment(), PlaylistAdapter.ItemClickListener {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaylistsViewModel>()
    private var playListAdapter: PlaylistAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playListAdapter = PlaylistAdapter(this)

        with(binding.playlistPlaylistView) {
            layoutManager = GridLayoutManager(context, 2)
            adapter = playListAdapter
        }

        viewModel.state().observe(viewLifecycleOwner, Observer {
            when (it) {
                is PlaylistState.Loading -> {
                    binding.playlistPlaylistView.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.errorImage.visibility = View.GONE
                    binding.errorText.visibility = View.GONE
                }
                is PlaylistState.Empty -> {
                    binding.playlistPlaylistView.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.errorImage.visibility = View.VISIBLE
                    binding.errorText.visibility = View.VISIBLE
                }
                is PlaylistState.Content -> {
                    playListAdapter?.setData(it.list)
                    binding.playlistPlaylistView.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    binding.errorImage.visibility = View.GONE
                    binding.errorText.visibility = View.GONE
                }
            }
        })

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_libraryFragment_to_playlistFragment,
                PlaylistViewFragment.createArgs()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(position: Int, adapter: PlaylistAdapter) {
        val playlist = adapter.getData(position)
        findNavController().navigate(
            R.id.action_libraryFragment_to_playlistViewFragment,
            PlaylistViewFragment.createArgs(playlist.uuid)
        )
    }
}