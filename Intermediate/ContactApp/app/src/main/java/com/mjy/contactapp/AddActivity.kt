package com.mjy.contactapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mjy.contactapp.databinding.ActivityAddBinding

class AddActivity: AppCompatActivity() {

    private lateinit var binding : ActivityAddBinding
    private lateinit var contactViewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        binding.contactViewModel = contactViewModel

        initViews()
    }

    private fun initViews() {
        var enable = false
        binding.addButton.isEnabled = enable

        binding.nameEditText.addTextChangedListener {
            enable = binding.nameEditText.text.isNotEmpty() and binding.numberEditText.text.isNotEmpty()
            binding.addButton.isEnabled = enable
        }
        binding.numberEditText.addTextChangedListener {
            enable = binding.nameEditText.text.isNotEmpty() and binding.numberEditText.text.isNotEmpty()
            binding.addButton.isEnabled = enable
        }
        binding.addButton.setOnClickListener {
            Log.d("LOG", "Button Clicked")
            contactViewModel.addContactItem(
                binding.nameEditText.text!!.toString(),
                binding.numberEditText.text!!.toString()
            )
            finish()
        }
    }
}