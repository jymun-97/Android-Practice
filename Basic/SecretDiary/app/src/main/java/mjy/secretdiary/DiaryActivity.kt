package mjy.secretdiary

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity : AppCompatActivity() {
    private val editTextDiary: EditText by lazy {
        findViewById(R.id.editTextDiary)
    }
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        initViews()
        bindViews()
    }

    private fun initViews() {
        editTextDiary.setText(getSharedPreferences("diary", Context.MODE_PRIVATE)
            .getString("content", "")
        )
    }

    private fun bindViews() {
        val runnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                putString("content", editTextDiary.text.toString())
            }
        }
        editTextDiary.addTextChangedListener {
            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 500)
        }
    }
}