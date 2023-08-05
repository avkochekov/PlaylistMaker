package av.kochekov.playlistmaker.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.presentation.model.TrackInfo

class TrackListAdapter(private val itemClickListener: ItemClickListener? = null) : RecyclerView.Adapter<TrackListHolder> () {
    private var data = mutableListOf<TrackInfo>()

    interface ItemClickListener {
        fun onItemClick(position: Int, adapter: TrackListAdapter)
    }

    lateinit var holder: TrackListHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_list_item, parent, false)
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

    fun setData(trackInfoList: List<TrackInfo>){
        data = trackInfoList as MutableList<TrackInfo>
        notifyDataSetChanged()
    }

    fun getData(index: Int): TrackInfo {
        return data.elementAt(index)
    }

    fun clearData(){
        data = mutableListOf<TrackInfo>()
        notifyDataSetChanged()
    }
}