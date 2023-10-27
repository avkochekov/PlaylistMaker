package av.kochekov.playlistmaker.library.presentation.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.databinding.FragmentFavoriteTracksBinding
import av.kochekov.playlistmaker.player.presentation.PlayerActivity
import av.kochekov.playlistmaker.search.presentation.TrackListAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L

class FavoriteTracksFragment : Fragment(), TrackListAdapter.ItemClickListener {

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FavoriteTracksViewModel>()

    private var trackListAdapter: TrackListAdapter? = null

    private var isClickAllowed = true

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

        errorPlaceholder.root.visibility = View.GONE
        errorPlaceholder.errorPlaceholderText.text =
            getString(R.string.library_error_favoriteTracks)
        errorPlaceholder.errorPlaceholderImage.setImageResource(R.drawable.search_error)
        errorPlaceholder.errorPlaceholderButton.visibility = View.GONE

        trackList.root.visibility = View.GONE
        trackList.root.adapter = trackListAdapter

        viewModel.activityState().observe(viewLifecycleOwner, Observer {
            when(it){
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
        if (clickDebounce()){
            val track = adapter.getData(position)
            startActivity(Intent(activity, PlayerActivity::class.java).apply {
                putExtra(PlayerActivity.TRACK, track)
            })
        }
    }

    private fun clickDebounce(): Boolean {
        if (isClickAllowed){
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
            return true
        }
        return false
    }
}