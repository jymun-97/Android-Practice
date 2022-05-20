package com.mjy.mvvm_practice_2;

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.mjy.mvvm_practice_2.databinding.ActivityFragmentContainerBinding

class FragmentContainerActivity : AppCompatActivity(){

    private lateinit var binding: ActivityFragmentContainerBinding

    private val seekBarFragment = SeekBarFragment()
    private val switchFragment = SwitchFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentContainerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .apply {
                add(binding.frameLayout1.id, seekBarFragment)
                add(binding.frameLayout2.id, switchFragment)
                commit()
            }
    }
}
