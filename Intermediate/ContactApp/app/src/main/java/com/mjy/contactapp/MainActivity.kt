package com.mjy.contactapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mjy.contactapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        binding.contactViewModel = contactViewModel

        contactViewModel.contactItemList.observe(this, Observer<ArrayList<ContactItem>> {
            Log.d("LOG", "Observe!")
            contactAdapter.setData(it)
        })

        initRecyclerView()
        initAddButton()
    }

    private fun initRecyclerView() {
        contactAdapter = ContactAdapter(this, contactViewModel.itemList)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = contactAdapter
        }
    }

    private fun initAddButton() {
        binding.addButton.setOnClickListener {
            startActivity(
                Intent(this, AddActivity::class.java)
            )
        }
    }
}