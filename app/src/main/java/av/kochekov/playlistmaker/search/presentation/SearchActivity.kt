package av.kochekov.playlistmaker.search.presentation

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import av.kochekov.playlistmaker.App
import av.kochekov.playlistmaker.search.domain.model.ErrorMessageType
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.creator.TrackListCreator
import av.kochekov.playlistmaker.player.presentation.PlayerActivity
import av.kochekov.playlistmaker.creator.RepositoryCreator
import av.kochekov.playlistmaker.search.domain.model.SearchActivityState

class SearchActivity : AppCompatActivity(), TrackListAdapter.ItemClickListener {

    private var searchEditText: EditText? = null
    private var searchClearButton: ImageView? = null

    private var updateButton: Button? = null

    private var errorPlaceholder: LinearLayout? = null
    private var errorPlaceholderText: TextView? = null
    private var errorPlaceholderImage: ImageView? = null

    private var progressBar: ProgressBar? = null

    private var trackListView: RecyclerView? = null
    private var trackListAdapter: TrackListAdapter? = null

    private var trackListHistoryLayout: LinearLayout? = null
    private var trackListHistoryView: RecyclerView? = null
    private var trackListHistoryAdapter: TrackListAdapter? = null

    private var historyClearButton: Button? = null

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this, SearchViewModel.getSearchModelFactory(
            trackListInteractor = TrackListCreator.provideTrackListInteractor(),
            searchHistoryInteractor = RepositoryCreator.provideSearchHistoryInteractor(App.preferences!!)
        )).get(SearchViewModel::class.java)


        viewModel.searchText().observe(this, Observer {
            searchClearButton?.isVisible = it.isNotEmpty()
            if (searchEditText?.text.toString() == it)
                return@Observer
            searchEditText?.setText(it)
        })
        viewModel.activityState().observe(this, Observer {
            when (it){
                is SearchActivityState.HistoryList -> {
                    trackListHistoryAdapter?.setData(it.trackList)
                    progressBar?.visibility = View.GONE
                    errorPlaceholder?.visibility = View.GONE
                    updateButton?.visibility = View.GONE
                    trackListView?.visibility = View.GONE
                    trackListHistoryLayout?.isVisible = it.trackList.isNotEmpty()
                }
                is SearchActivityState.SearchResultList -> {
                    trackListAdapter?.setData(it.trackList)
                    progressBar?.visibility = View.GONE
                    errorPlaceholder?.visibility = View.GONE
                    updateButton?.visibility = View.GONE
                    trackListView?.visibility = View.VISIBLE
                    trackListHistoryLayout?.visibility = View.GONE
                }
                is SearchActivityState.InSearchActivity -> {
                    progressBar?.visibility = View.VISIBLE
                    errorPlaceholder?.visibility = View.GONE
                    updateButton?.visibility = View.GONE
                    trackListView?.visibility = View.GONE
                    trackListHistoryLayout?.visibility = View.GONE
                }
                is SearchActivityState.Error -> {
                    progressBar?.visibility = View.GONE
                    trackListView?.visibility = View.GONE
                    trackListHistoryLayout?.visibility = View.GONE
                    errorPlaceholder?.visibility = View.VISIBLE
                    when(it.error) {
                        ErrorMessageType.NO_CONNECTION -> {
                            errorPlaceholderImage?.setImageResource(R.drawable.connection_error)
                            errorPlaceholderText?.text = getString(R.string.search_error_connectionFailed)
                            updateButton?.visibility = View.VISIBLE
                        }
                        ErrorMessageType.NO_DATA -> {
                            errorPlaceholderImage?.setImageResource(R.drawable.search_error)
                            errorPlaceholderText?.text = getString(R.string.search_error_emptyTrackList)
                            updateButton?.visibility = View.GONE
                        }
                    }
                }
            }
        })

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressed()
        }

        progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        searchClearButton = findViewById<ImageView>(R.id.search_clear).apply {
            setOnClickListener{
                viewModel.clearSearchText()
            }
        }
        searchEditText = findViewById<EditText>(R.id.searchField).apply {
            requestFocus()
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.setSearchText(s.toString())

                }

                override fun afterTextChanged(s: Editable?) { }
            })
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.search()
                    true
                }
                false
            }
        }

        errorPlaceholder = findViewById(R.id.errorPlaceholder)
        errorPlaceholderText = findViewById(R.id.errorPlaceholderText)
        errorPlaceholderImage = findViewById(R.id.errorPlaceholderImage)

        findViewById<Button>(R.id.errorPlaceholderButton).apply {
            text = getString(R.string.search_update)
            setOnClickListener{
                viewModel.search()
            }
        }

        trackListAdapter = TrackListAdapter(this)
        trackListHistoryAdapter = TrackListAdapter(this)

        trackListView = findViewById<RecyclerView>(R.id.trackList).apply {
            adapter = trackListAdapter
        }
        trackListHistoryView = findViewById<RecyclerView>(R.id.trackListHistory).apply {
            adapter = trackListHistoryAdapter
        }
        trackListHistoryLayout = findViewById<LinearLayout>(R.id.trackListHistoryLayout).apply {
            isVisible = (trackListHistoryView?.size!! > 0)
        }

        historyClearButton = findViewById<Button>(R.id.trackListHistory_Clear).apply {
            setOnClickListener {
                viewModel.clearHistory()
            }
        }
    }

    override fun onItemClick(position: Int, adapter: TrackListAdapter) {
        val track = adapter.getData(position)
        viewModel.addToHistory(track)
        startActivity(Intent(this, PlayerActivity::class.java).apply {
            putExtra(PlayerActivity.TRACK, track)
        })
    }
}