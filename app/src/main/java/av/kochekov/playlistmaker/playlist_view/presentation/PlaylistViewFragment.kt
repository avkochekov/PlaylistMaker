package av.kochekov.playlistmaker.playlist_view.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.databinding.FragmentPlaylistViewBinding
import av.kochekov.playlistmaker.player.presentation.PlayerFragment
import av.kochekov.playlistmaker.playlist_editor.presentation.PlaylistEditorFragment
import av.kochekov.playlistmaker.playlist_view.presentation.utils.Formatter
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistViewFragment : Fragment(), TrackListAdapter.ItemClickListener {
    private var _binding: FragmentPlaylistViewBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaylistViewViewModel>()

    private var trackListAdapter: TrackListAdapter? = null

    companion object {
        const val PLAYLIST_UUID = "CurrentPlaylistUUID"
        fun createArgs(data: String = ""): Bundle =
            bundleOf(PLAYLIST_UUID to data)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.load(requireArguments().get(PLAYLIST_UUID).toString())

        trackListAdapter = TrackListAdapter(this)
        binding.playlistTrackList.adapter = trackListAdapter
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.playlistData().observe(viewLifecycleOwner, Observer { data ->
            binding.playlistTitle.text = data.name
            binding.playlistDescription.text = data.description
            binding.playlistDescription.isVisible = data.description.isNotEmpty()

            binding.playlistTotalTracks.text =
                view.context.applicationContext.resources.getQuantityString(
                    R.plurals.tracks,
                    data.tracks.size,
                    data.tracks.size
                )

            val totalTime = Formatter.getTotalTime(data.tracks)
            binding.playlistTotalTime.text =
                view.context.applicationContext.resources.getQuantityString(
                    R.plurals.minutes,
                    totalTime,
                    totalTime
                )

            trackListAdapter?.setData(data.tracks)

            Glide.with(view)
                .load(data.artwork)
                .placeholder(R.drawable.placeholder)
                .into(binding.playlistArtwork)

            binding.menuPlaylistInfo.name.text = data.name
            binding.menuPlaylistInfo.tracksCount.text = view.context.applicationContext.resources.getQuantityString(
                R.plurals.tracks,
                data.tracks.size,
                data.tracks.size
            )

            Glide.with(view)
                .load(data.artwork)
                .placeholder(R.drawable.placeholder)
                .into(binding.menuPlaylistInfo.playlistArtwork)

        })

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = 1 + slideOffset
            }
        })

        binding.menuButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.shareButton.setOnClickListener {
            sharePlaylist()
        }

        binding.menuShareButton.setOnClickListener {
            sharePlaylist()
        }

        binding.menuEditeButton.setOnClickListener{
            viewModel.playlistData().value?.let {playlist ->
                findNavController().navigate(
                    R.id.action_playlistViewFragment_to_playlistEditorFragment,
                    PlaylistEditorFragment.createArgs(playlist.uuid)
                )
            }
        }

        binding.menuRemoveButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            activity?.let { activity ->
                MaterialAlertDialogBuilder(activity)
                    .setMessage(R.string.playlist_confirmDeletePlaylist_message)
                    .setPositiveButton(R.string.playlist_confirmDeletePlaylist_positiveButton) { _, _ ->
                        viewModel.removePlaylist()
                        findNavController().navigateUp()
                    }
                    .setNegativeButton(R.string.playlist_confirmDeletePlaylist_negativeButton) { _, _ ->
                        // Do nothing
                    }
                    .show()
            }
        }
    }

    override fun onItemClick(position: Int, adapter: TrackListAdapter) {
        trackListAdapter?.let { adapter ->
            val track = adapter.getData(position)
            findNavController().navigate(
                R.id.action_playlistViewFragment_to_playerFragment,
                PlayerFragment.createArgs(track.trackId)
            )
        }
    }

    override fun onItemLongClick(position: Int, adapter: TrackListAdapter) {
        activity?.let { activity ->
            MaterialAlertDialogBuilder(activity)
                .setMessage(R.string.playlist_confirmDeleteTrack_message)
                .setPositiveButton(R.string.playlist_confirmDeleteTrack_positiveButton) { _, _ ->
                    viewModel.removeTrack(adapter.getData(position))
                }
                .setNegativeButton(R.string.playlist_confirmDeleteTrack_negativeButton) { _, _ ->
                    // Do nothing
                }
                .show()
        }
    }

    private fun sharePlaylist(){
        trackListAdapter?.let { adapter ->
            if (adapter.itemCount == 0)
                Toast.makeText(
                    context,
                    getString(R.string.playlist_error_emptyPlaylist),
                    Toast.LENGTH_LONG
                ).show()
            else
                viewModel.share()
        }
    }
}