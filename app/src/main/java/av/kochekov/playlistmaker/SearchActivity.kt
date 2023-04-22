package av.kochekov.playlistmaker

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.internal.ViewUtils

class SearchActivity : AppCompatActivity() {
    companion object{
        const val SEARCH_QUERY = "SEARCH_QUERY"
        var query:String = String()
    }

    private lateinit var searchEditText: EditText
    private lateinit var searchClearButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            onBackPressed()
        }
        searchEditText = findViewById<EditText>(R.id.search_query)
        searchClearButton = findViewById<ImageView>(R.id.search_clear)

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                query = s.toString()
                searchClearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) { }
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)

        searchClearButton.setOnClickListener {
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(searchEditText.windowToken, 0) // скрыть клавиатуру
            searchEditText.clearFocus()
            searchEditText.setText("")
        }
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
}