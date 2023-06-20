package av.kochekov.playlistmaker

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

    companion object {
        const val TRACK = "CurrentTrackInfo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressed()
        }

        artwork = findViewById(R.id.artwork)
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)

        duration = findViewById(R.id.duration)
        album = findViewById(R.id.album)
        release = findViewById(R.id.release)
        genre = findViewById(R.id.genre)
        country = findViewById(R.id.country)

        (intent.getSerializableExtra(TRACK) as? Track)?.let {
            bindTrackData(it)
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

        Glide.with(this)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(artwork.resources.getDimensionPixelSize(R.dimen.audioPlayer_artworkRadius)))
            .into(artwork)
    }
}