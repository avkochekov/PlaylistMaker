package av.kochekov.playlistmaker

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.coroutines.NonDisposableHandle.parent
import java.text.SimpleDateFormat
import java.util.*


class TrackListHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val artwork: ImageView = itemView.findViewById(R.id.artwork)

    companion object {
        const val TRACK = "CurrentTrackInfo"
    }

    fun bind(item: Track) {
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.trackItemImage_round)))
            .into(artwork)
    }

    fun onClickListner(item: Track){
        val intent = Intent(itemView.context, AudioPlayerActivity::class.java)
        intent.putExtra(AudioPlayerActivity.TRACK, item);
        itemView.context.startActivity(intent)
    }
}
