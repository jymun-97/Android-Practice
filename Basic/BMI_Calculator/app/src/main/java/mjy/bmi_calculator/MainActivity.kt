package mjy.bmi_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val editTextHeight: EditText by lazy {
        findViewById(R.id.editTextHeight)
    }
    private val editTextWeight: EditText by lazy {
        findViewById(R.id.editTextWeight)
    }
    private val button: Button by lazy {
        findViewById(R.id.button)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            if (editTextHeight.text.isEmpty() || editTextWeight.text.isEmpty()) {
                Toast.makeText(this, "모든 값을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var height = editTextHeight.text.toString().toDouble() / 100.0
            var weight = editTextWeight.text.toString().toDouble()

            var bmi = weight / height / height
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("BMI", bmi)
            startActivity(intent)
        }
    }
}