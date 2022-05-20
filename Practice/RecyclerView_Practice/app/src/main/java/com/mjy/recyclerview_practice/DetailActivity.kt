package com.mjy.recyclerview_practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.mjy.recyclerview_practice.databinding.ActivityDetailBinding

class DetailActivity: AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivityDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val name = intent.getStringExtra(IntentKeys.NAME)
        val phone = intent.getStringExtra(IntentKeys.PHONE)

        binding.let {
            it.nameTextView.text = name
            it.phoneTextView.text = phone
            it.okButton.setOnClickListener {
                finish()
            }
        }
    }
}