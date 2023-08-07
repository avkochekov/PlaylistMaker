package av.kochekov.playlistmaker.presentation.player
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import av.kochekov.playlistmaker.MediaPlayerCreator
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.presentation.model.TrackInfo
import av.kochekov.playlistmaker.domain.mediaplayer.api.MediaPlayerStateListenerInterface
import av.kochekov.playlistmaker.domain.mediaplayer.model.MediaPlayerState
import av.kochekov.playlistmaker.presentation.Formatter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlayerActivity : AppCompatActivity(), MediaPlayerStateListenerInterface {
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

    private var playerState = MediaPlayerState.STATE_DEFAULT
    private val timeUpdateRunnable = Runnable { updateRemainingTime() }
    private val handler = Handler(Looper.getMainLooper())

    private val playerInteractor = MediaPlayerCreator.provideMediaPlayerInteractor();

    companion object {
        const val TRACK = "CurrentTrackInfo"
        private const val TIME_UPDATE_VALUE_MILLIS = 200L
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)
        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressed()
        }
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
            bindTrackData(it)
        }
        play?.setOnClickListener {
            playbackControl()
        }

        playerInteractor.setListener(this)
    }
    fun bindTrackData(trackInfo: TrackInfo){
        trackInfo.previewUrl?.run {
            playerInteractor.setTrack(this)
        }

        trackName?.text = trackInfo.trackName
        artistName?.text = trackInfo.artistName
        duration?.text = trackInfo.duration
        album?.text = trackInfo.collectionName
        release?.text = trackInfo.releaseYear
        genre?.text = trackInfo.primaryGenreName
        country?.text = trackInfo.country

        artwork?.let {
            Glide.with(this)
                .load(trackInfo.artworkUrl512)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .transform(RoundedCorners(it.resources.getDimensionPixelSize(R.dimen.audioPlayer_artworkRadius)))
                .into(it)
        }
        updateRemainingTime()
    }
    private fun playbackControl() {
        when(playerState) {
            MediaPlayerState.STATE_PLAYING -> {
                playerInteractor.pause()
            }
            MediaPlayerState.STATE_PREPARED,
            MediaPlayerState.STATE_PAUSED -> {
                playerInteractor.play()
            }
        }
    }
    private fun updateButtonImage() {
        when(playerState){
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
    }
    override fun onPause() {
        super.onPause()
        playerInteractor.pause()
    }
    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.stop()
    }
    private fun updateRemainingTime(){
        when(playerState){
            MediaPlayerState.STATE_DEFAULT,
            MediaPlayerState.STATE_PREPARED -> {
                trackTime?.text = Formatter.timeToText(0)
                handler.removeCallbacks(timeUpdateRunnable)
            }
            MediaPlayerState.STATE_PLAYING -> {
                trackTime?.text = Formatter.timeToText(playerInteractor.timePosition())
                handler.postDelayed(timeUpdateRunnable, TIME_UPDATE_VALUE_MILLIS)
            }
            MediaPlayerState.STATE_PAUSED -> {
                handler.removeCallbacks(timeUpdateRunnable)
            }
        }
    }

    override fun onStateChanged(state: Int) {
        playerState = state
        updateButtonImage()
        updateRemainingTime()
    }
}