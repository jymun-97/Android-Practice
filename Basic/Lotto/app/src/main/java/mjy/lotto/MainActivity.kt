package mjy.lotto

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {
    private val numberPicker: NumberPicker by lazy {
        findViewById(R.id.numberPicker)
    }
    private val buttonAdd: Button by lazy {
        findViewById(R.id.buttonAdd)
    }
    private val buttonReset: Button by lazy {
        findViewById(R.id.buttonReset)
    }
    private val buttonAutoCreate: Button by lazy {
        findViewById(R.id.buttonAutoCreate)
    }
    private val numberList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.textViewNumber1),
            findViewById(R.id.textViewNumber2),
            findViewById(R.id.textViewNumber3),
            findViewById(R.id.textViewNumber4),
            findViewById(R.id.textViewNumber5),
            findViewById(R.id.textViewNumber6)
        )
    }
    private var count: Int = 0
    private var isSelected = arrayOfNulls<Boolean>(45 + 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        bindViews()
    }

    private fun initViews() {
        numberPicker.apply {
            minValue = 1
            maxValue = 45
        }
        clear()
    }

    private fun clear() {
        for (i in 1..45)
            isSelected[i] = false
        count = 0

        numberList.forEach { target ->
            target.isVisible = false
        }
    }

    private fun bindViews() {
        buttonAdd.setOnClickListener {
            if (count >= 6) {
                Toast.makeText(this, "모든 번호를 선택하였습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val number = numberPicker.value
                if (isSelected[number] == true) {
                    Toast.makeText(this, "이미 선택된 번호입니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                addNumber(number)
            }
        }

        buttonReset.setOnClickListener {
            clear()
        }

        buttonAutoCreate.setOnClickListener {
            if (count >= 6) {
                Toast.makeText(this, "모든 번호를 선택하였습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val randomNumberList = makeRandomNumbers()
                randomNumberList.forEach { number ->
                    if (count < 6 && isSelected[number] == false) {
                        addNumber(number)
                    }
                }
            }
        }
    }

    private fun addNumber(number: Int) {
        numberList[count].apply {
            isVisible = true
            text = number.toString()
        }
        setBackgroundColor(numberList[count])
        count++
        isSelected[number] = true
    }

    private fun makeRandomNumbers(): List<Int> {
        val list: MutableList<Int> = mutableListOf()
        list.apply {
            for (num in 1..45)
                add(num)
            shuffle()
        }
        return list
    }

    private fun setBackgroundColor(target: TextView) {
        when (target.text.toString().toInt()) {
            in 1..10 ->
                target.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 ->
                target.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 ->
                target.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 ->
                target.background = ContextCompat.getDrawable(this, R.drawable.circle_black)
            else ->
                target.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }
    }
}