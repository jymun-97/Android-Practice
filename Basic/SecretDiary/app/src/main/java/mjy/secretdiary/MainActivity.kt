package mjy.secretdiary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {
    private val numberPickerList: List<NumberPicker> by lazy {
        listOf<NumberPicker>(
            findViewById(R.id.numberPicker1),
            findViewById(R.id.numberPicker2),
            findViewById(R.id.numberPicker3)
        )
    }
    private val buttonOpen: AppCompatButton by lazy {
        findViewById(R.id.buttonOpen)
    }
    private val buttonChange: AppCompatButton by lazy {
        findViewById(R.id.buttonChange)
    }
    private var password: String = "000"
    private var changeMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        bindViews()
    }

    private fun initViews() {
        numberPickerList.forEach { numberPicker ->
            numberPicker.apply {
                minValue = 0
                maxValue = 9
            }
        }
        password = getSharedPreferences("password", Context.MODE_PRIVATE)
            .getString("password", "000")
            .toString()
    }

    private fun bindViews() {
        buttonOpen.setOnClickListener {
            if (changeMode) {
                Toast.makeText(this, "비밀번호 변경을 완료해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (isCorrectPassWord()) {
                    startActivity(Intent(this, DiaryActivity::class.java))
                } else {
                    showError()
                }
            }
        }

        buttonChange.setOnClickListener {
            if (changeMode.not()) {
                if (isCorrectPassWord()) {
                    changeMode = true
                    buttonChange.setBackgroundColor(Color.RED)
                } else {
                    showError()
                }
            } else {
                changeMode = false
                buttonChange.setBackgroundColor(Color.BLACK)
                savePassword()
                Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isCorrectPassWord(): Boolean = (password == readInput())
    private fun readInput(): String {
        var input = ""
        numberPickerList.forEach { numberPicker ->
            input = input.plus(numberPicker.value)
        }
        return input
    }

    private fun showError() {
        AlertDialog.Builder(this)
            .setTitle("오류")
            .setMessage("잘못된 비밀번호 입니다.")
            .setPositiveButton("확인") { _, _ -> }
            .create()
            .show()
    }

    private fun savePassword() {
        password = readInput()
        getSharedPreferences("password", Context.MODE_PRIVATE).edit() {
            putString("password", readInput())
        }
    }
}