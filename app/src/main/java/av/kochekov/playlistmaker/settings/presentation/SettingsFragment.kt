package av.kochekov.playlistmaker.settings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import av.kochekov.playlistmaker.databinding.FragmentSettingsBinding
import av.kochekov.playlistmaker.settings.presentation.composeUI.SettingsPage
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SettingsPage()
            }
        }
    }
}