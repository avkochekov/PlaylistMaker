package av.kochekov.playlistmaker
import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var artwork: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var duration: TextView
    private lateinit var album: TextView
    private lateinit var release: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var play: ImageButton
    private var mediaPlayer = MediaPlayer()
    private lateinit var trackTime: TextView
    private var playerState = STATE_DEFAULT
    private val timeUpdateRunnable = Runnable { updateRemainingTime() }
    private val handler = Handler(Looper.getMainLooper())
    companion object {
        const val TRACK = "CurrentTrackInfo"
        private const val TIME_UPDATE_VALUE_MILLIS = 200L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
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
        (intent.getSerializableExtra(TRACK) as? Track)?.let {
            bindTrackData(it)
        }
        play.setOnClickListener {
            playbackControl()
        }
    }
    fun bindTrackData(track: Track){
        trackName.text = track.trackName
        artistName.text = track.artistName
        duration.text = track.duration
        album.text = track.collectionName
        release.text = track.releaseYear
        genre.text = track.primaryGenreName
        country.text = track.country
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            updateButtonImage()
        }
        Glide.with(this)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(artwork.resources.getDimensionPixelSize(R.dimen.audioPlayer_artworkRadius)))
            .into(artwork)
        updateRemainingTime()
    }
    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    private fun pausePlayer(){
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        updateRemainingTime()
        updateButtonImage()
    }
    private fun startPlayer(){
        mediaPlayer.start()
        playerState = STATE_PLAYING
        updateRemainingTime()
        updateButtonImage()
    }
    private fun updateButtonImage() {
        when(playerState){
            STATE_PLAYING -> {
                play.setBackgroundResource(R.drawable.audioplayer_pause_button)
            }
            STATE_PREPARED, STATE_PAUSED -> {
                play.setBackgroundResource(R.drawable.audioplayer_play_button)
            }
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
    private fun updateRemainingTime(){
        when(playerState){
            STATE_DEFAULT -> {
                trackTime.text = "00:00"
            }
            STATE_PLAYING -> {
                trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                handler.postDelayed(timeUpdateRunnable, AudioPlayerActivity.TIME_UPDATE_VALUE_MILLIS)
            }
            STATE_PAUSED -> {
                handler.removeCallbacks(timeUpdateRunnable)
            }
            STATE_PREPARED -> {
                handler.removeCallbacks(timeUpdateRunnable)
                trackTime.text = "00:00"
            }
        }
    }
}