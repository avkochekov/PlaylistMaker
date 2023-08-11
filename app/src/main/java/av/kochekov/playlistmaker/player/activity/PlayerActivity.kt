package av.kochekov.playlistmaker.player.activity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import av.kochekov.playlistmaker.creator.MediaPlayerCreator
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.search.models.TrackInfo
import av.kochekov.playlistmaker.player.models.MediaPlayerState
import av.kochekov.playlistmaker.player.view_model.PlayerViewModel
import av.kochekov.playlistmaker.utils.Formatter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlayerActivity : AppCompatActivity() {
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

    private lateinit var viewModel: PlayerViewModel

    companion object {
        const val TRACK = "CurrentTrackInfo"
        private const val TIME_UPDATE_VALUE_MILLIS = 200L
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        viewModel = ViewModelProvider(this, PlayerViewModel.getPlayerModelFactory(
            mediaPlayerInteractor = MediaPlayerCreator.provideMediaPlayerInteractor(),
        )).get(PlayerViewModel::class.java)

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressed()
        }

        viewModel.trackInfo().observe(this, Observer {
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

        viewModel.playerState().observe(this, Observer {
            when(it){
                MediaPlayerState.STATE_DEFAULT -> {
                    play?.isEnabled = false
                    play?.setBackgroundResource(R.drawable.audioplayer_play_button)
                }
                MediaPlayerState.STATE_PLAYING -> {
                    play?.isEnabled = true
                    play?.setBackgroundResource(R.drawable.audioplayer_pause_button)
                }
                MediaPlayerState.STATE_PAUSED,
                MediaPlayerState.STATE_PREPARED-> {
                    play?.isEnabled = true
                    play?.setBackgroundResource(R.drawable.audioplayer_play_button)
                }
            }
        })

        viewModel.trackPosition().observe(this, Observer {
            trackTime?.text = Formatter.timeToText(it)
        })

        play = findViewById(R.id.playButton)
        artwork = findViewById(R.id.artwork)
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        duration = findViewById(R.id.duration)
        album = findViewById(R.id.album)
        release = findViewById(R.id.release)
        genre = findViewById(R.id.genre)
        country = findViewById(R.id.country)
        trackTime = findViewById(R.id.trackTime)
        (intent.getSerializableExtra(TRACK) as? TrackInfo)?.let {
            viewModel.setTrack(it)
        }
        play?.setOnClickListener {
            viewModel.onPlayClicked()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopPlayer()
    }
}