package av.kochekov.playlistmaker.search.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.databinding.FragmentSearchBinding
import av.kochekov.playlistmaker.player.presentation.PlayerFragment
import av.kochekov.playlistmaker.search.domain.model.ErrorMessageType
import av.kochekov.playlistmaker.search.domain.model.SearchFragmentState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L

class SearchFragment : Fragment(), TrackListAdapter.ItemClickListener {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SearchViewModel>()

    private var trackListAdapter: TrackListAdapter? = null
    private var historyListAdapter: TrackListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackListAdapter = TrackListAdapter(this)
        historyListAdapter = TrackListAdapter(this)

        val progressBar = binding.progressBar
        val errorPlaceholder = binding.errorPlaceholderLayout
        val trackList = binding.trackListLayout
        val historyList = binding.historyListLayout

        trackList.root.adapter = trackListAdapter
        historyList.trackList.root.adapter = historyListAdapter

        viewModel.fragmentState().observe(viewLifecycleOwner, Observer {
            when (it) {
                is SearchFragmentState.HistoryList -> {
                    historyListAdapter?.setData(it.trackList)
                    historyList.root.isVisible = it.trackList.isNotEmpty()
                    trackList.root.isVisible = false
                    progressBar.isVisible = false
                    errorPlaceholder.root.isVisible = false
                }
                is SearchFragmentState.SearchResultList -> {
                    trackListAdapter?.setData(it.trackList)
                    trackList.root.isVisible = true
                    historyList.root.isVisible = false
                    progressBar.isVisible = false
                    errorPlaceholder.root.isVisible = false
                }
                is SearchFragmentState.InSearchActivity -> {
                    progressBar.isVisible = true
                    trackList.root.isVisible = false
                    historyList.root.isVisible = false
                    errorPlaceholder.root.isVisible = false
                }
                is SearchFragmentState.Error -> {
                    binding.progressBar.isVisible = false
                    trackList.root.isVisible = false
                    historyList.root.isVisible = false
                    errorPlaceholder.root.isVisible = true
                    errorPlaceholder.errorPlaceholderButton.apply {
                        text = getString(R.string.search_update)
                        setOnClickListener {
                            viewModel.search()
                        }
                    }
                    when (it.error) {
                        ErrorMessageType.NO_CONNECTION -> {
                            errorPlaceholder.errorPlaceholderImage.setImageResource(R.drawable.connection_error)
                            errorPlaceholder.errorPlaceholderText.text =
                                getString(R.string.search_error_connectionFailed)
                            errorPlaceholder.errorPlaceholderButton.isVisible = true
                        }
                        ErrorMessageType.NO_DATA -> {
                            errorPlaceholder.errorPlaceholderImage.setImageResource(R.drawable.search_error)
                            errorPlaceholder.errorPlaceholderText.text =
                                getString(R.string.search_error_emptyTrackList)
                            errorPlaceholder.errorPlaceholderButton.isVisible = true
                        }
                    }
                }
            }
        })

        binding.searchClear.setOnClickListener {
            binding.searchField.text?.clear()
        }

        binding.historyListLayout.trackListHistoryClear.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.searchField.apply {
            requestFocus()
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    viewModel.search(s.toString())
                    binding.searchClear.isVisible = s.toString().isNotEmpty()
                }

                override fun afterTextChanged(s: Editable?) {}
            })
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.search()
                    true
                }
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.breakSearch()
        _binding = null
    }

    override fun onItemClick(position: Int, adapter: TrackListAdapter) {
        val track = adapter.getData(position)
        viewModel.addToHistory(track)
        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }
}