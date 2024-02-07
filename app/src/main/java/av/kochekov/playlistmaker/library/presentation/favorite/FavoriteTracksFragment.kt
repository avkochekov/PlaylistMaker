package av.kochekov.playlistmaker.library.presentation.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.databinding.FragmentFavoriteTracksBinding
import av.kochekov.playlistmaker.player.presentation.PlayerFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment(), TrackListAdapter.ItemClickListener {
    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private var trackListAdapter: TrackListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val errorPlaceholder = binding.errorPlaceholderLayout
        val trackList = binding.trackList
        val progressBar = binding.progressBar

        trackListAdapter = TrackListAdapter(this)

        with(errorPlaceholder) {
            root.visibility = View.GONE
            errorPlaceholderText.text = getString(R.string.library_error_favoriteTracks)
            errorPlaceholderImage.setImageResource(R.drawable.search_error)
            errorPlaceholderButton.visibility = View.GONE
        }

        with(trackList) {
            root.visibility = View.GONE
            root.adapter = trackListAdapter
        }

        viewModel.activityState().observe(viewLifecycleOwner, Observer {
            when (it) {
                is FavoriteTrackListState.Empty -> {
                    errorPlaceholder.root.visibility = View.VISIBLE
                    trackList.root.visibility = View.GONE
                    progressBar.visibility = View.GONE
                }
                is FavoriteTrackListState.Loading -> {
                    errorPlaceholder.root.visibility = View.GONE
                    trackList.root.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                is FavoriteTrackListState.Content -> {
                    errorPlaceholder.root.visibility = View.GONE
                    trackList.root.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    trackListAdapter?.setData(it.tracks)
                }
            }
        })
        viewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int, adapter: TrackListAdapter) {
        val data = adapter.getData(position)
        findNavController().navigate(
            R.id.action_libraryFragment_to_playerFragment,
            PlayerFragment.createArgs(data.trackId)
        )
    }
}