package av.kochekov.playlistmaker.library.presentation.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.library.domain.favorite.models.TrackModel

class TrackListAdapter(private val itemClickListener: ItemClickListener? = null) :
    RecyclerView.Adapter<TrackListHolder>() {
    private var data = mutableListOf<TrackModel>()

    interface ItemClickListener {
        fun onItemClick(position: Int, adapter: TrackListAdapter)
    }

    lateinit var holder: TrackListHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_list_item, parent, false)
        holder = TrackListHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: TrackListHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(position, this)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(trackModelList: List<TrackModel>) {
        data = trackModelList as MutableList<TrackModel>
        notifyDataSetChanged()
    }

    fun getData(index: Int): TrackModel {
        return data.elementAt(index)
    }

}