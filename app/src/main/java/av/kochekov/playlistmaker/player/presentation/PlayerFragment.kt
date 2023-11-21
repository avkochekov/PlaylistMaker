package av.kochekov.playlistmaker.player.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.databinding.FragmentPlayerBinding
import av.kochekov.playlistmaker.search.domain.model.TrackModel
import av.kochekov.playlistmaker.player.domain.models.MediaPlayerState
import av.kochekov.playlistmaker.player.domain.models.PlaylistListState
import av.kochekov.playlistmaker.player.presentation.models.MessageState
import av.kochekov.playlistmaker.player.presentation.utils.Formatter
import av.kochekov.playlistmaker.playlist_editor.presentation.PlaylistEditorFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment(), PlaylistAdapter.ItemClickListener {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private var artwork: ImageView? = null
    private var trackName: TextView? = null
    private var artistName: TextView? = null
    private var duration: TextView? = null
    private var album: TextView? = null
    private var release: TextView? = null
    private var genre: TextView? = null
    private var country: TextView? = null
    private var play: ImageButton? = null
    private var trackTime: TextView? = null
    private var favoriteButton: ImageButton? = null
    private var addToPlaylistButton: ImageButton? = null

    private val viewModel by viewModel<PlayerViewModel>()
    private var playListAdapter: PlaylistAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TRACK = "CurrentTrackInfo"
        const val TRACK_ID = "CurrentTrackId"
        fun createArgs(track: TrackModel): Bundle =
            bundleOf(TRACK to track)
        fun createArgs(track: Int): Bundle =
            bundleOf(TRACK_ID to track)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireArguments().get(TRACK)?.let {data ->
            viewModel.setTrack(data as TrackModel)
        }
        requireArguments().get(TRACK_ID)?.let { data ->
            viewModel.setTrack(data as Int)
        }
        playListAdapter = PlaylistAdapter(this)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        play = binding.playButton
        artwork = binding.artwork
        trackName = binding.trackName
        artistName = binding.artistName
        duration = binding.duration
        album = binding.album
        release = binding.release
        genre = binding.genre
        country = binding.country
        trackTime = binding.trackTime
        favoriteButton = binding.addToFavoriteButton
        addToPlaylistButton = binding.addToPlaylistButton

        binding.playerPlaylistView.adapter = playListAdapter

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_playlistFragment,
            PlaylistEditorFragment.createArgs())
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playerBottomSheet)
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

        viewModel.playlistState().observe(viewLifecycleOwner, Observer {
            when (it) {
                is PlaylistListState.Empty -> {
                    // Do nothing
                }
                is PlaylistListState.Data -> {
                    playListAdapter?.setData(it.list)
                }
            }
        })

        viewModel.trackInFavorite().observe(viewLifecycleOwner, Observer {
            if (it) {
                favoriteButton?.setImageResource(R.drawable.ic_favorite)
            } else {
                favoriteButton?.setImageResource(R.drawable.ic_not_favorite)
            }
        })

        viewModel.trackInfo().observe(viewLifecycleOwner, Observer {
            trackName?.text = it.trackName
            artistName?.text = it.artistName
            duration?.text = it.duration
            album?.text = it.collectionName
            release?.text = it.releaseYear
            genre?.text = it.primaryGenreName
            country?.text = it.country

            val artworkUrl = it.artworkUrl512
            artwork?.let { it ->
                Glide.with(this)
                    .load(artworkUrl)
                    .placeholder(R.drawable.placeholder)
                    .fitCenter()
                    .transform(RoundedCorners(it.resources.getDimensionPixelSize(R.dimen.audioPlayer_artworkRadius)))
                    .into(it)
            }
        })

        viewModel.playerState().observe(viewLifecycleOwner, Observer {
            when (it) {
                MediaPlayerState.STATE_DEFAULT -> {
                    play?.isEnabled = false
                    play?.setBackgroundResource(R.drawable.audioplayer_play_button)
                }
                MediaPlayerState.STATE_PLAYING -> {
                    play?.isEnabled = true
                    play?.setBackgroundResource(R.drawable.audioplayer_pause_button)
                }
                MediaPlayerState.STATE_PAUSED,
                MediaPlayerState.STATE_PREPARED -> {
                    play?.isEnabled = true
                    play?.setBackgroundResource(R.drawable.audioplayer_play_button)
                }
            }
        })

        viewModel.trackPosition().observe(viewLifecycleOwner, Observer {
            trackTime?.text = Formatter.timeToText(it)
        })

        viewModel.message().observe(viewLifecycleOwner, Observer {
            when (it) {
                is MessageState.TrackAlreadyInPlaylist -> {
                    Toast.makeText(
                        context,
                        getString(R.string.addToPlaylist_alreadyContains, it.playlist),
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.clearMessage()
                }
                is MessageState.AddTrackToPlaylistSuccess -> {
                    Toast.makeText(
                        context,
                        getString(R.string.addToPlaylist_success, it.playlist),
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.clearMessage()
                }
                else -> {}
            }

        })

        play?.setOnClickListener {
            viewModel.onPlayClicked()
        }

        favoriteButton?.setOnClickListener {
            viewModel.changeFavoriteState()
        }

        addToPlaylistButton?.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        viewModel.loadPlaylists()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopPlayer()
        _binding = null
    }

    override fun onItemClick(position: Int, adapter: PlaylistAdapter) {
        val data = adapter.getData(position)
        viewModel.addToPlaylist(data)
    }
}