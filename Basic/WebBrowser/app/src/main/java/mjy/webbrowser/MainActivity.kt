package mjy.webbrowser

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private val buttonHome: ImageButton by lazy {
        findViewById(R.id.buttonHome)
    }
    private val buttonBack: ImageButton by lazy {
        findViewById(R.id.buttonBack)
    }
    private val buttonForword: ImageButton by lazy {
        findViewById(R.id.buttonForward)
    }
    private val editTextAddress: EditText by lazy {
        findViewById(R.id.editTextAddress)
    }
    private val progressBar: ContentLoadingProgressBar by lazy {
        findViewById(R.id.progressBar)
    }
    private val swipeRefreshLayout: SwipeRefreshLayout by lazy {
        findViewById(R.id.swipeRefreshLayout)
    }
    private val webView: WebView by lazy {
        findViewById(R.id.webView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        bindViews()
    }

    private fun initViews() {
        webView.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            loadUrl(HOME)
        }
    }

    private fun bindViews() {
        buttonBack.setOnClickListener { webView.goBack() }
        buttonForword.setOnClickListener { webView.goForward() }
        buttonForword.setOnClickListener { webView.loadUrl(HOME) }
        swipeRefreshLayout.setOnRefreshListener { webView.reload() }

        editTextAddress.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val url = v.text.toString()

                if (URLUtil.isNetworkUrl(url))
                    webView.loadUrl(url)
                else
                    webView.loadUrl("http://${v.text}")
            }
            return@setOnEditorActionListener false
        }
    }

    inner class WebViewClient: android.webkit.WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            progressBar.hide()
            swipeRefreshLayout.isRefreshing = false

            buttonBack.isEnabled = webView.canGoBack()
            buttonForword.isEnabled = webView.canGoForward()
            editTextAddress.setText(url)
        }
    }

    inner class WebChromeClient(): android.webkit.WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            progressBar.progress = newProgress
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
            return
        }
        super.onBackPressed()
    }

    companion object {
        private const val HOME = "http://www.google.com"
    }
}