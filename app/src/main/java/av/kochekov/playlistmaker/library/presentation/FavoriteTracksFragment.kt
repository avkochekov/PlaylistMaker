package av.kochekov.playlistmaker.library.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import av.kochekov.playlistmaker.databinding.FragmentFavoriteTracksBinding

class FavoriteTracksFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }

    private var binding: FragmentFavoriteTracksBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding?.root
    }
}