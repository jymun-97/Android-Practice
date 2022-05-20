package mjy.calculator

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.room.Room
import mjy.calculator.model.History

class MainActivity : AppCompatActivity() {
    private val textViewExpression: TextView by lazy {
        findViewById(R.id.textViewExpression)
    }
    private val textViewResult: TextView by lazy {
        findViewById(R.id.textViewResult)
    }
    private val historyLayout: ConstraintLayout by lazy {
        findViewById(R.id.constraintLayoutHistory)
    }
    private val linearLayoutHistory: LinearLayout by lazy {
        findViewById(R.id.linearLayoutHistory)
    }
    private var operatorFlag = false
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()
    }

    fun clearButtonClicked(view: View) {
        textViewExpression.text = ""
        textViewResult.text = ""
        operatorFlag = false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun operButtonClicked(view: View) {
        if (textViewExpression.text.isEmpty()) return
        if (operatorFlag) {
            Toast.makeText(this, "하나의 연산자만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val operator = (view as Button).text.toString()
        textViewExpression.append(" $operator ")
        operatorFlag = true

        val ssb = SpannableStringBuilder(textViewExpression.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.textGreen)),
            textViewExpression.text.length - 2,
            textViewExpression.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textViewExpression.text = ssb
    }

    fun equalButtonClikced(view: View) {
        if (textViewResult.text.isEmpty()) {
            Toast.makeText(this, "수식을 완성하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val expression = textViewExpression.text.toString()
        val result = textViewResult.text.toString()
        textViewExpression.text = result
        textViewResult.text = ""
        operatorFlag = false

        Thread(Runnable {
            db.historyDao().insertHistory(
                History(
                    null,
                    expression,
                    result
                )
            )
        }).start()
    }

    fun numButtonClikced(view: View) {
        val target = textViewExpression.text.split(" ").last()
        if (target.length < 15) {
            val number = (view as Button).text.toString()
            if (target.isEmpty() && number == "0") {
                Toast.makeText(this, "0이 먼저 올 수 없습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            textViewExpression.append("$number")
            if (operatorFlag) textViewResult.text = calculate()
        } else {
            Toast.makeText(this, "최대 15자리의 수를 계산할 수 있습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculate(): String {
        val expressions = textViewExpression.text.split(" ")
        val num1 = expressions[0].toBigInteger()
        val num2 = expressions[2].toBigInteger()

        return when (expressions[1]) {
            "+" -> (num1 + num2).toString()
            "-" -> (num1 - num2).toString()
            "x" -> (num1 * num2).toString()
            "÷" -> (num1 / num2).toString()
            "%" -> (num1 % num2).toString()
            else -> ""
        }
    }

    fun historyButtonClicked(view: View) {
        historyLayout.isVisible = true
        linearLayoutHistory.removeAllViews()

        Thread(Runnable {
            db.historyDao().getAll().reversed().forEach {
                runOnUiThread {
                    val historyView = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                    historyView.findViewById<TextView>(R.id.textViewResultHistory).text = it.result
                    historyView.findViewById<TextView>(R.id.textViewExpressionHistory).text = it.expression
                    linearLayoutHistory.addView(historyView)
                }
            }
        }).start()
    }

    fun buttonCloseHistoryClicked(view: android.view.View) {
        historyLayout.isVisible = false
    }

    fun buttonDeleteHistoryClicked(view: android.view.View) {
        linearLayoutHistory.removeAllViews()
        Thread(Runnable {
            db.historyDao().deleteAll()
        }).start()
    }

    fun historyRowClicked(view: android.view.View) {
        textViewExpression.text = view.findViewById<TextView>(R.id.textViewExpressionHistory).text
        textViewResult.text = view.findViewById<TextView>(R.id.textViewResultHistory).text

        historyLayout.isVisible = false
    }
}