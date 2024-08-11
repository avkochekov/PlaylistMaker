package av.kochekov.playlistmaker.player.presentation

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.databinding.FragmentPlayerBinding
import av.kochekov.playlistmaker.player.domain.models.PlaylistListState
import av.kochekov.playlistmaker.player.presentation.custom_ui.PlaybackButtonView
import av.kochekov.playlistmaker.player.presentation.models.MessageState
import av.kochekov.playlistmaker.playlist_editor.presentation.PlaylistEditorFragment
import av.kochekov.playlistmaker.common.domain.TrackModel
import av.kochekov.playlistmaker.services.AudioPlayerControl
import av.kochekov.playlistmaker.services.MusicService
import av.kochekov.playlistmaker.services.NotificationControl
import av.kochekov.playlistmaker.services.PlayerState
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
    private var play: PlaybackButtonView? = null
    private var trackTime: TextView? = null
    private var favoriteButton: ImageButton? = null
    private var addToPlaylistButton: ImageButton? = null

    private val viewModel by viewModel<PlayerViewModel>()
    private var playListAdapter: PlaylistAdapter? = null

    private var bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback? = null

    private var musicService: MusicService? = null

    private var playerState: PlayerState = PlayerState.Default()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicServiceBinder
            binder.getService().let {musicService ->
                viewModel.setAudioPlayerControl(musicService as AudioPlayerControl)
                viewModel.setNotificationControl(musicService as NotificationControl)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.removeAudioPlayerControl()
            viewModel.removeNotificationControl()
        }
    }

    private fun bindMusicService() {
        val intent = Intent(requireContext(), MusicService::class.java).apply {
            putExtra("song_url", viewModel.trackInfo().value?.previewUrl)
            putExtra("artist", viewModel.trackInfo().value?.artistName)
            putExtra("track", viewModel.trackInfo().value?.trackName)
        }

        context?.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindMusicService() {
        context?.unbindService(serviceConnection)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Если выдали разрешение — привязываемся к сервису.
            bindMusicService()
        } else {
            // Иначе просто покажем ошибку
            Toast.makeText(requireContext(), "Can't bind service!", Toast.LENGTH_LONG).show()
        }
    }

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

        requireArguments().get(TRACK)?.let { data ->
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
            findNavController().navigate(
                R.id.action_playerFragment_to_playlistFragment,
                PlaylistEditorFragment.createArgs()
            )
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playerBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetCallback = object :
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
        }

        bottomSheetCallback?.let { bottomSheetBehavior.addBottomSheetCallback(it) }

        play?.isEnabled = viewModel.playerState().value !is PlayerState.Default
        play?.setPlayState(viewModel.playerState().value is PlayerState.Playing)
        trackTime?.text = viewModel.playerState().value?.progress?:"00:00"

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

            // Прежде чем привязаться к сервису, который будет показывать уведомление
            // Мы должны проверить, выданы ли соответствующие разрешения
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                // На версиях ниже Android 13 —
                // можно сразу привязаться к сервису.
                bindMusicService()
            }

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

        viewModel.playerState().observe(viewLifecycleOwner, Observer {state ->
            when (state) {
                is PlayerState.Default -> {
                    play?.isEnabled = false
                    play?.setPlayState(false)
                    trackTime?.text = "00:00"
                }

                is PlayerState.Prepared -> {
                    play?.isEnabled = true
                    trackTime?.text = "00:00"
                    play?.setPlayState(false)
                }

                is PlayerState.Playing -> {
                    play?.isEnabled = true
                    trackTime?.text = state.progress
                    play?.setPlayState(true)
                }

                is PlayerState.Paused -> {
                    play?.isEnabled = true
                    trackTime?.text = state.progress
                    play?.setPlayState(false)
                }
            }
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

        favoriteButton?.setOnClickListener {
            viewModel.changeFavoriteState()
        }

        addToPlaylistButton?.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        play?.setOnClickListener {
            viewModel.onPlayerButtonClicked()
        }

        viewModel.loadPlaylists()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindMusicService()
        // I don’t know how else to untie the callback so that the application doesn’t crash
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playerBottomSheet)
        bottomSheetCallback?.let {
            bottomSheetBehavior.removeBottomSheetCallback(it)
            bottomSheetCallback = null
        }
        _binding = null
    }

    override fun onItemClick(position: Int, adapter: PlaylistAdapter) {
        val data = adapter.getData(position)
        viewModel.addToPlaylist(data)
    }
}