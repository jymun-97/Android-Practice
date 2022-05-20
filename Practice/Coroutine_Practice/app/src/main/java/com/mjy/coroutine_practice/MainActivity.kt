package com.mjy.coroutine_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.mjy.coroutine_practice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val progressViewModel = ViewModelProvider(this)[ProgressViewModel::class.java]
        binding.lifecycleOwner = this
        binding.progressViewModel = progressViewModel
    }
}