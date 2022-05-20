package com.mjy.mvvm_practice_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mjy.mvvm_practice_2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CountViewModel::class.java]

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.intentButton.setOnClickListener {
            startActivity(
                Intent(this, FragmentContainerActivity::class.java)
            )
        }
    }
}