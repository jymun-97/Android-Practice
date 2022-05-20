package mjy.bmi_calculator

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    private val textViewBMI: TextView by lazy {
        findViewById(R.id.textViewBMI)
    }
    private val textViewResult: TextView by lazy {
        findViewById(R.id.textViewResult)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        initViews()
    }

    private fun initViews() {
        val bmi = intent.getDoubleExtra("BMI", 0.0).toString()
        textViewBMI.setText(textViewBMI.text.toString().plus(bmi))

        val result = getResult(bmi.toDouble())
        textViewResult.setText(textViewResult.text.toString().plus(result))
    }

    private fun getResult(bmi: Double): String {
        return when (bmi) {
            in 0.0..18.5 -> "저체중"
            in 18.5..23.0 -> "정상체중"
            in 23.0..25.0 -> "과체중"
            in 25.0..30.0 -> "경도 비만"
            in 30.0..35.0 -> "중정도 비만"
            else -> "고도 비만"
        }
    }
}