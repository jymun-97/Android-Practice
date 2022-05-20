package com.mjy.fragment_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.mjy.fragment_practice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)


        val home = HomeFragment()
        val list = ListFragment()
        val user = UserFragment()

        replaceFragment(home)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeItem -> {
                    replaceFragment(home)
                }
                R.id.listItem -> {
                    replaceFragment(list)
                }
                R.id.userItem -> {
                    replaceFragment(user)
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(binding.fragmentContainerView.id, fragment)
                commit()
            }
    }
}