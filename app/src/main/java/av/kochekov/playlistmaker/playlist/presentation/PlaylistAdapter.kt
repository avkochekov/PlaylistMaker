package av.kochekov.playlistmaker.playlist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.playlist.domain.models.PlaylistModel

class PlaylistAdapter : RecyclerView.Adapter<PlaylistHolder>() {

    lateinit var holder: PlaylistHolder

    private var data = mutableListOf<PlaylistModel>()

    fun setData(list: List<PlaylistModel>) {
        data = list as MutableList<PlaylistModel>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_grid_item, parent, false)
        holder = PlaylistHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
//            itemClickListener?.onItemClick(position, this)
        }
    }
}