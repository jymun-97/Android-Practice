package com.mjy.mvvm_practice_2

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mjy.mvvm_practice_2.databinding.FragmentSeekbarBinding

class SeekBarFragment: Fragment(R.layout.fragment_seekbar) {

    private lateinit var binding: FragmentSeekbarBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSeekbarBinding.bind(view)

        val viewModel = ViewModelProvider(requireActivity())[ShareViewModel::class.java]

        binding.lifecycleOwner = requireActivity()
        binding.viewModel = viewModel

        binding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    viewModel.updateNumber(p1)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}

                override fun onStopTrackingTouch(p0: SeekBar?) {}

            }
        )
    }
}