package com.mjy.mvvm_practice_2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel: ViewModel() {

    private val _number = MutableLiveData<Int>(0)
    val number : LiveData<Int>
        get() = _number

    private val _stateString = MutableLiveData<String>("OFF")
    val stateString : LiveData<String>
        get() = _stateString

    private val _state = MutableLiveData<Boolean>(false)
    val state : LiveData<Boolean>
        get() = _state

    fun updateNumber(value: Int) {
        _number.value = value

        if (_number.value!! >= 50) {
            _stateString.value = "ON"
            _state.value = true
        }
        else {
            _stateString.value = "OFF"
            _state.value = false
        }
    }
}