package av.kochekov.playlistmaker.library.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import av.kochekov.playlistmaker.library.presentation.favorite.FavoriteTracksFragment
import av.kochekov.playlistmaker.library.presentation.playlists.PlaylistsFragment

enum class LibraryTab {
    FAVORITE_TRACKS_TAB,
    PLAY_LISTS_TAB
}

class LibraryPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (LibraryTab.values()[position]) {
            LibraryTab.FAVORITE_TRACKS_TAB -> FavoriteTracksFragment.newInstance()
            LibraryTab.PLAY_LISTS_TAB -> PlaylistsFragment.newInstance()
        }
    }
}