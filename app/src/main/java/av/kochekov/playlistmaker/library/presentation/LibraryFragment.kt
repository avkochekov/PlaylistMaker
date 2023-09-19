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

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private var tabMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = LibraryPagerAdapter(childFragmentManager, lifecycle)

        tabMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = resources.getText(R.string.library_favoriteTracks)
                    1 -> tab.text = resources.getText(R.string.library_playLists)
                }
            }
        tabMediator?.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator?.detach()
        _binding = null
    }
}