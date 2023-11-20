package av.kochekov.playlistmaker.library.presentation.playlists

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.library.domain.playlists.models.PlaylistModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlaylistHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val artwork: ImageView = itemView.findViewById(R.id.playlistArtwork)
    private val name: TextView = itemView.findViewById(R.id.name)
    private val count: TextView = itemView.findViewById(R.id.tracksCount)

    fun bind(item: PlaylistModel) {
        name.text = item.name
        count.text = itemView.context.applicationContext.resources.getQuantityString(
            R.plurals.tracks,
            item.tracksCount,
            item.tracksCount
        )

        if (item.artwork.isEmpty()) {
            artwork.setImageURI(Uri.parse(item.artwork))
        } else {
            artwork.setImageResource(R.drawable.placeholder)
        }

        Glide.with(itemView)
            .load(item.artwork)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.playlistItemImage_round)))
            .into(artwork)
    }
}