package com.mjy.mvvm_practice_2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mjy.mvvm_practice_2.databinding.FragmentSwitchBinding

class SwitchFragment: Fragment(R.layout.fragment_switch) {

    private lateinit var binding: FragmentSwitchBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSwitchBinding.bind(view)

        val viewModel = ViewModelProvider(requireActivity())[ShareViewModel::class.java]

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel
    }
}