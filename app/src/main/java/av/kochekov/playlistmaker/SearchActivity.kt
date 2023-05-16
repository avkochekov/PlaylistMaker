package av.kochekov.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Random

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
    private lateinit var errorNoConnection: LinearLayout
    private lateinit var errorNoData: LinearLayout

    private val retrofit = Retrofit.Builder()
        .baseUrl(ITunesApi.apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    private val trackListAdapter = TrackListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressed()
        }
        searchEditText = findViewById<EditText>(R.id.search_query)
        searchClearButton = findViewById<ImageView>(R.id.search_clear)

        updateButton = findViewById(R.id.update)
        errorNoData = findViewById<LinearLayout>(R.id.noDataMessage)
        errorNoConnection = findViewById<LinearLayout>(R.id.noConnectionMessage)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                query = s.toString()
                searchClearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) { }
        })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getTrackList()
                true
            }
            false
        }

        searchClearButton.setOnClickListener {
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(searchEditText.windowToken, 0)
            searchEditText.clearFocus()
            searchEditText.setText("")
            getTrackList()
        }

        updateButton.setOnClickListener {
            getTrackList()
        }

        findViewById<RecyclerView>(R.id.trackList).adapter = trackListAdapter
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

    private fun showErrorMessage(type: ErrorMessageType){
        hideErrorMessage()
        when (type) {
            ErrorMessageType.NO_CONNECTION -> {
                errorNoConnection.visibility = View.VISIBLE
            }
            ErrorMessageType.NO_DATA -> {
                errorNoData.visibility = View.VISIBLE
            }
        }
    }

    private fun hideErrorMessage(){
        errorNoData.visibility = View.GONE
        errorNoConnection.visibility = View.GONE
    }

    private fun getTrackList(){
        hideErrorMessage()
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
                                hideErrorMessage()
                                trackListAdapter.setData(response.body()?.results!!)
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