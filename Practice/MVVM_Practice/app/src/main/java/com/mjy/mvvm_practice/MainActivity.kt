package com.mjy.mvvm_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.mjy.mvvm_practice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var numberViewModel: NumberViewModel
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        numberViewModel = ViewModelProvider(this)[NumberViewModel::class.java]
        numberViewModel.currentValue.observe(this, Observer {
            Log.d("MainActivity", "Observed!")
            binding.numberTextView.text = it.toString()
        })

        initViews()
    }

    private fun initViews() {
        binding.apply {
            plusButton.setOnClickListener {
                val input = inputEditText.text.toString()
                val number = if (input.isNotBlank()) input.toInt() else 0
                numberViewModel.updateValue(Action.PLUS, number)
            }
            minusButton.setOnClickListener {
                val input = inputEditText.text.toString()
                val number = if (input.isNotBlank()) input.toInt() else 0
                numberViewModel.updateValue(Action.MINUS, number)
            }
        }
    }
}