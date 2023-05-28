package av.kochekov.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object{
        const val SEARCH_QUERY = "SEARCH_QUERY"
        private var query:String = String()
    }

    enum class ErrorMessageType {
        NO_DATA,
        NO_CONNECTION
    }

    private lateinit var searchEditText: EditText
    private lateinit var searchClearButton: ImageView

    private lateinit var updateButton: Button

    private lateinit var errorPlaceholder: LinearLayout
    private lateinit var errorPlaceholderText: TextView
    private lateinit var errorPlaceholderImage: ImageView

    private lateinit var preferences: SharedPreferences

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITunesApi.apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    private lateinit var trackListView: RecyclerView
    private lateinit var trackListAdapter: TrackListAdapter

    private lateinit var trackListHistoryLayout: LinearLayout
    private lateinit var trackListHistoryView: RecyclerView
    private lateinit var trackListHistoryAdapter: TrackListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        preferences = getSharedPreferences(applicationInfo.loadLabel(packageManager).toString(), MODE_PRIVATE)
        trackListAdapter = TrackListAdapter(preferences)
        trackListHistoryAdapter = TrackListAdapter()

        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressed()
        }

        preferences.apply {
            var listener = OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == SearchHistory.DATA_KEY){
                    trackListHistoryAdapter.setData(SearchHistory(sharedPreferences).get())
                }
            }
            registerOnSharedPreferenceChangeListener(listener)
        }

        searchClearButton = findViewById<ImageView>(R.id.search_clear).apply {
            setOnClickListener{
                val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                keyboard.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                searchEditText.clearFocus()
                searchEditText.setText("")
                getTrackList()
                showTrackListHistory()
            }
        }
        searchEditText = findViewById<EditText>(R.id.searchField).apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    query = s.toString()
                    searchClearButton.visibility = clearButtonVisibility(s)
                }

                override fun afterTextChanged(s: Editable?) { }
            })
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    getTrackList()
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
                getTrackList()
            }
        }

        trackListView = findViewById<RecyclerView>(R.id.trackList).apply {
            adapter = trackListAdapter
        }
        trackListHistoryView = findViewById<RecyclerView>(R.id.trackListHistory).apply {
                adapter = trackListHistoryAdapter
                trackListHistoryAdapter.setData(SearchHistory(preferences).get())
            }
        trackListHistoryLayout = findViewById<LinearLayout>(R.id.trackListHistoryLayout).apply {
            visibility = if (trackListHistoryView.size > 0) View.VISIBLE else View.GONE
        }
        findViewById<Button>(R.id.trackListHistory_Clear).setOnClickListener {
            SearchHistory(preferences).clear()
            showTrackListHistory()
        }
        showTrackListHistory()
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            searchEditText.clearFocus()
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        query = savedInstanceState.getString(SEARCH_QUERY).toString()
        searchEditText.setText(query)
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
                errorPlaceholderImage.setImageResource(R.drawable.connection_error)
                errorPlaceholderText.text = getString(R.string.search_error_connectionFailed)
                updateButton.visibility = View.VISIBLE
            }
            ErrorMessageType.NO_DATA -> {
                errorPlaceholderImage.setImageResource(R.drawable.search_error)
                errorPlaceholderText.text = getString(R.string.search_error_emptyTrackList)
            }
            else -> return
        }
        errorPlaceholder.visibility = View.VISIBLE
    }

    private fun hideErrorMessage(){
        errorPlaceholder.visibility = View.GONE
    }

    private fun showTrackList(){
        trackListHistoryLayout.visibility = View.GONE
        trackListView.visibility = View.VISIBLE
    }

    private fun showTrackListHistory(){
        SearchHistory(preferences).get().let{
            trackListHistoryAdapter.setData(it)
            trackListHistoryLayout.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun hideTrackListHistory(){
        trackListHistoryLayout.visibility = View.GONE
    }

    private fun hideTrackList(){
        findViewById<LinearLayout>(R.id.trackListHistoryLayout).visibility = if (trackListHistoryView.size > 0) View.VISIBLE else View.GONE
    }

    private fun getTrackList(){
        hideTrackList()
        hideTrackListHistory()
        hideErrorMessage()
        trackListView.visibility = View.GONE
        trackListAdapter.clearData()
        if (searchEditText.text.isNotEmpty()){
            iTunesService.search(searchEditText.text.toString()).enqueue(object : Callback<TrackResponse>{
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    when (response.code()){
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackListAdapter.setData(response.body()?.results!!)
                                hideErrorMessage()
                                showTrackList()
                            } else {
                                showErrorMessage(ErrorMessageType.NO_DATA)
                            }
                        }
                        else -> showErrorMessage(ErrorMessageType.NO_DATA)
                    }
                }

                override fun onFailure(
                    call: Call<TrackResponse>,
                    t: Throwable
                ) {
                    showErrorMessage(ErrorMessageType.NO_CONNECTION)
                }
            })
        }
    }
}