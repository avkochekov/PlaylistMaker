package av.kochekov.playlistmaker.player.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.databinding.FragmentPlayerBinding
import av.kochekov.playlistmaker.search.domain.model.TrackInfo
import av.kochekov.playlistmaker.player.domain.models.MediaPlayerState
import av.kochekov.playlistmaker.player.presentation.utils.Formatter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val TRACK = "CurrentTrackInfo"
        fun createArgs(track: TrackInfo): Bundle =
            bundleOf(TRACK to track)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setTrack(requireArguments().get(TRACK) as TrackInfo)

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

        viewModel.trackInFavorite().observe(viewLifecycleOwner, Observer {
            if (it){
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

        play?.setOnClickListener {
            viewModel.onPlayClicked()
        }

        favoriteButton?.setOnClickListener{
            viewModel.changeFavoriteState()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopPlayer()
    }
}