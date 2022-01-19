package com.mjy.cleanarchitecture_practice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mjy.cleanarchitecture_practice.databinding.ActivityMainBinding
import com.mjy.cleanarchitecture_practice.ui.view.FavoriteFragment
import com.mjy.cleanarchitecture_practice.ui.view.SearchFragment
import com.mjy.cleanarchitecture_practice.ui.view.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigationView()
        if (savedInstanceState == null) {
            showFragment(SearchFragment())
        }
    }

    private fun initBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_search -> showFragment(SearchFragment())
                R.id.fragment_favorite -> showFragment(FavoriteFragment())
                R.id.fragment_settings -> showFragment(SettingsFragment())
            }
            true
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, fragment)
            .commit()
    }
}