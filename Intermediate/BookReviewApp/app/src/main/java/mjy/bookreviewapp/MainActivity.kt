package mjy.bookreviewapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import mjy.bookreviewapp.adapter.BookAdapter
import mjy.bookreviewapp.adapter.HistoryAdapter
import mjy.bookreviewapp.api.BookService
import mjy.bookreviewapp.databinding.ActivityMainBinding
import mjy.bookreviewapp.model.BestSellerDto
import mjy.bookreviewapp.model.History
import mjy.bookreviewapp.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var bookService: BookService
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = getAppDatabase(this)

        initViews()
        initData()
    }

    private fun initViews() {
        initBookRecyclerView()
        initHistoryRecyclerView()
        initSearchEditText()
        initFloatingButton()
    }

    private fun initBookRecyclerView() {
        adapter = BookAdapter(
            itemClickedListener = {
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("bookModel", it)
                startActivity(intent)
            }
        )
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(
            historyTextViewClickedListener = {
                search(it)
            },
            historyDeletedClickedListener = {
                deleteSearchKeyword(it)
            }
        )
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun initSearchEditText() {
        hideKeyBoard()
        binding.searchEditText.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }

            return@setOnKeyListener false
        }

        binding.searchEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }
    }

    private fun initFloatingButton() {
        binding.floatingButton.setOnClickListener {
            binding.searchEditText.requestFocus()
            showKeyBoard()
            showHistoryView()
        }
    }

    private fun search(keyword: String) {
        if (keyword.isBlank()) {
            initData()
            hideHistoryView()
            hideKeyBoard()
            return
        }
        bookService.getBooksByName(getString(R.string.interParkAPIKey), keyword)
            .enqueue(object: Callback<SearchBookDto> {

                override fun onResponse(
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>
                ) {
                    if (response.isSuccessful.not()) return

                    saveSearchKeyword(keyword)
                    hideHistoryView()
                    hideKeyBoard()
                    adapter.submitList(response.body()?.books.orEmpty())
                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    hideHistoryView()
                    Log.e(TAG, t.toString())
                }
            })
    }

    private fun saveSearchKeyword(keyword: String){
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
            showHistoryView()
        }.start()
    }

    private fun initData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)
        bookService.getBestSellerBooks(getString(R.string.interParkAPIKey))
            .enqueue(object: Callback<BestSellerDto> {

                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {
                    if (response.isSuccessful.not()) return
                    response.body()?.let {
                        adapter.submitList(it.books)
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    Log.e(TAG, t.toString())
                }
            })
    }

    private fun showHistoryView() {
        Thread {
            val keywords = db.historyDao().getAll().reversed()

            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()
        binding.historyRecyclerView.isVisible = true
    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    private fun showKeyBoard() {
        val inputMethodManager = binding.searchEditText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun hideKeyBoard() {
        val inputMethodManager = binding.searchEditText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    companion object {
        private const val BASE_API_URL = "https://book.interpark.com"
        private const val TAG = "MainActivity"
    }
}