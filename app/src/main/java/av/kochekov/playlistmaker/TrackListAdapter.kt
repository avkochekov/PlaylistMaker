package av.kochekov.playlistmaker

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.annotations.Nullable

class TrackListAdapter(private val itemClickListener: ItemClickListener? = null) : RecyclerView.Adapter<TrackListHolder> () {
    private var data = mutableListOf<Track>()

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_list_item, parent, false)
        return TrackListHolder(view)
    }

    override fun onBindViewHolder(holder: TrackListHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(trackList: List<Track>){
        data = trackList as MutableList<Track>
        notifyDataSetChanged()
    }

    fun getData(index: Int): Track {
        return data.elementAt(index)
    }

    fun clearData(){
        data = mutableListOf<Track>()
        notifyDataSetChanged()
    }
}