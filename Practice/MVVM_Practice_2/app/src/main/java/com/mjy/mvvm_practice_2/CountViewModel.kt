package com.mjy.mvvm_practice_2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountViewModel: ViewModel() {

    private val _count = MutableLiveData<Int>(0)
    val count: LiveData<Int>
        get() = _count

    fun plus() {
        _count.value = (_count.value ?: 0) + 1
    }

    fun minus() {
        _count.value = (_count.value ?: 0) - 1
    }
}
