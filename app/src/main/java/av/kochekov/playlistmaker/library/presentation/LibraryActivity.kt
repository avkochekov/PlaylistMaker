package av.kochekov.playlistmaker.library.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LibraryActivity : AppCompatActivity() {

    private var tabMediator: TabLayoutMediator? = null
    private var binding: ActivityLibraryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.viewPager?.adapter = LibraryPagerAdapter(supportFragmentManager, lifecycle)


        tabMediator = TabLayoutMediator(binding?.tabLayout!!, binding?.viewPager!!) { tab, position ->
            when(position) {
                0 -> tab.text = resources.getText(R.string.library_favoriteTracks)
                1 -> tab.text = resources.getText(R.string.library_playLists)
            }
        }
        tabMediator?.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator?.detach()
    }
}