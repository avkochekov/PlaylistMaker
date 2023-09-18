package av.kochekov.playlistmaker.library.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.databinding.FragmentLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment : Fragment() {

    private var binding: FragmentLibraryBinding? = null

    private var tabMediator: TabLayoutMediator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentLibraryBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            binding?.viewPager?.adapter = LibraryPagerAdapter(it.supportFragmentManager, lifecycle)

            tabMediator = TabLayoutMediator(binding?.tabLayout!!, binding?.viewPager!!) { tab, position ->
                when(position) {
                    0 -> tab.text = resources.getText(R.string.library_favoriteTracks)
                    1 -> tab.text = resources.getText(R.string.library_playLists)
                }
            }
            tabMediator?.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator?.detach()
    }
}