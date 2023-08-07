package av.kochekov.playlistmaker.presentation.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Looper.*
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import av.kochekov.playlistmaker.presentation.model.ErrorMessageType
import av.kochekov.playlistmaker.R
import av.kochekov.playlistmaker.SearchHistoryRepositoryCreator
import av.kochekov.playlistmaker.TrackListCreator
import av.kochekov.playlistmaker.domain.search.api.TrackListInteractor
import av.kochekov.playlistmaker.domain.search.model.Track
import av.kochekov.playlistmaker.presentation.player.PlayerActivity
import av.kochekov.playlistmaker.presentation.model.TrackInfo

class SearchActivity : AppCompatActivity(), TrackListAdapter.ItemClickListener, TrackListInteractor.TrackConsumer {
    companion object{
        const val SEARCH_QUERY = "SEARCH_QUERY"
        private var query:String = String()
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }

    private var isClickAllowed = true

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

    private val searchRunnable = Runnable { getTrackList() }
    private val handler = Handler(Looper.getMainLooper())

    private val trackListInteractor = TrackListCreator.provideTrackListInteractor()
    private val searchHistoryInteractor by lazy { SearchHistoryRepositoryCreator().provideSearchHistoryInteractor(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        trackListAdapter = TrackListAdapter(this)
        trackListHistoryAdapter = TrackListAdapter(this)

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressed()
        }

        showTrackListHistory()

        progressBar = findViewById<ProgressBar>(R.id.progress_bar)

        searchClearButton = findViewById<ImageView>(R.id.search_clear).apply {
            setOnClickListener{
                hideErrorMessage()
                val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                searchEditText?.let {
                    keyboard.hideSoftInputFromWindow(it.windowToken, 0)
                    it.clearFocus()
                    it.setText("")
                }
            }
        }
        searchEditText = findViewById<EditText>(R.id.searchField).apply {
            requestFocus()
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    query = s.toString()
                    searchClearButton?.visibility = clearButtonVisibility(s)
                    if (query.isEmpty()){
                        showTrackListHistory()
                    } else {
                        searchDebounce()
                    }
                }

                override fun afterTextChanged(s: Editable?) { }
            })
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    search()
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
                searchDebounce()
            }
        }

        trackListView = findViewById<RecyclerView>(R.id.trackList).apply {
            adapter = trackListAdapter
        }
        trackListHistoryView = findViewById<RecyclerView>(R.id.trackListHistory).apply {
            adapter = trackListHistoryAdapter
        }
        trackListHistoryLayout = findViewById<LinearLayout>(R.id.trackListHistoryLayout).apply {
            isVisible = (trackListHistoryView?.size!! > 0)
        }
        findViewById<Button>(R.id.trackListHistory_Clear).setOnClickListener {
            searchHistoryInteractor.clear()
            showTrackListHistory()
        }
        showTrackListHistory()
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            searchEditText?.clearFocus()
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        query = savedInstanceState.getString(SEARCH_QUERY).toString()
        searchEditText?.setText(query)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, query)
    }

    @SuppressLint("ResourceType")
    private fun showErrorMessage(type: ErrorMessageType){
        hideErrorMessage()
        when (type) {
            ErrorMessageType.NO_CONNECTION -> {
                errorPlaceholderImage?.setImageResource(R.drawable.connection_error)
                errorPlaceholderText?.text = getString(R.string.search_error_connectionFailed)
                updateButton?.visibility = View.VISIBLE
            }
            ErrorMessageType.NO_DATA -> {
                errorPlaceholderImage?.setImageResource(R.drawable.search_error)
                errorPlaceholderText?.text = getString(R.string.search_error_emptyTrackList)
            }
        }
        errorPlaceholder?.visibility = View.VISIBLE
        progressBar?.visibility = View.GONE
    }

    private fun showProgressBar(){
        progressBar?.visibility = View.VISIBLE
        trackListHistoryLayout?.visibility = View.GONE
        trackListView?.visibility = View.GONE
        errorPlaceholder?.visibility = View.GONE
    }

    private fun hideProgressBar(){
        progressBar?.visibility = View.GONE
    }

    private fun hideErrorMessage(){
        errorPlaceholder?.visibility = View.GONE
    }

    private fun showTrackList(){
        progressBar?.visibility = View.GONE
        trackListHistoryLayout?.visibility = View.GONE
        trackListView?.visibility = View.VISIBLE
    }

    private fun showTrackListHistory(){
        val trackList = searchHistoryInteractor.get()
        progressBar?.visibility = View.GONE
        trackListView?.visibility = View.GONE
        trackListHistoryAdapter?.setData(trackList.map {
            TrackInfo(
                it.trackId,
                it.trackName,
                it.artistName,
                it.trackTimeMillis,
                it.artworkUrl100,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.previewUrl
            )
        })
        trackListHistoryLayout?.visibility = if (trackList.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun hideTrackListHistory(){
        trackListHistoryLayout?.visibility = View.GONE
    }

    private fun hideTrackList(){
        trackListHistoryLayout?.visibility = if (trackListHistoryView?.size!! > 0) View.VISIBLE else View.GONE
    }

    private fun getTrackList(){
        hideTrackList()
        hideTrackListHistory()
        hideErrorMessage()
        trackListView?.visibility = View.GONE
        trackListAdapter?.clearData()

        searchEditText?.let{
            if (it.text.isNotEmpty()){
                progressBar?.visibility = View.VISIBLE
                trackListInteractor.searchTracks(it.text.toString(), this)
            } else {
                progressBar?.visibility = View.GONE
                showTrackListHistory()
            }
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }

    override fun onItemClick(position: Int, adapter: TrackListAdapter) {
        if (clickDebounce()){
            startActivity(Intent(this, PlayerActivity::class.java).apply {
                putExtra(PlayerActivity.TRACK, adapter.getData(position))
            })
            searchHistoryInteractor.add(adapter.getData(position).let {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            })
        }
    }

    private fun search(){
        handler.removeCallbacks(searchRunnable)
        searchRunnable.run()
    }

    private fun searchDebounce(){
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    override fun consume(foundTracks: List<Track>?) {
        runOnUiThread {
            hideErrorMessage()
            hideTrackList()
            foundTracks
                ?.let { it ->
                    if (it.isEmpty())
                        showErrorMessage(ErrorMessageType.NO_DATA)
                    else
                        this.trackListAdapter?.setData(it.map {
                            TrackInfo(
                                it.trackId,
                                it.trackName,
                                it.artistName,
                                it.trackTimeMillis,
                                it.artworkUrl100,
                                it.collectionName,
                                it.releaseDate,
                                it.primaryGenreName,
                                it.country,
                                it.previewUrl
                            )
                        })
                    showTrackList()
                }
                ?: showErrorMessage(ErrorMessageType.NO_CONNECTION)
        }
    }
}