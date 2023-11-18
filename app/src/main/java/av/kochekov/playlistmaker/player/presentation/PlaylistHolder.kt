package av.kochekov.playlistmaker.player.presentation

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.playlist.domain.models.PlaylistModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlaylistHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val artwork: ImageView = itemView.findViewById(R.id.artwork)
    private val name: TextView = itemView.findViewById(R.id.name)
    private val count: TextView = itemView.findViewById(R.id.tracksCount)

    fun bind(item: PlaylistModel) {
        name.text = item.name
        count.text = itemView.context.applicationContext.resources.getQuantityString(
            R.plurals.tracks,
            item.tracksCount,
            item.tracksCount
        )

        Glide.with(itemView)
            .load(item.artwork)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.playlistItemImage_round)))
            .into(artwork)
    }
}