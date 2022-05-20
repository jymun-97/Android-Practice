package com.mjy.mvvm_practice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// 데이터의 변경
// 뷰모델은 데이터의 변경사항을 알려주는 라이브 데이터를 가지고 있음
class NumberViewModel : ViewModel() {

    // 라이브 데이터
    // 1. 뮤터블 라이브 데이터 -> 수정 가능
    // 2. 노멀 라이브 데이터 -> 읽기 전용

    private val _currentValue = MutableLiveData<Int>()
    val currentValue: LiveData<Int>
        get() = _currentValue

    init {
        _currentValue.value = 0
    }

    fun updateValue(action: Action, input: Int) {
        when (action) {
            Action.PLUS ->
                _currentValue.value = _currentValue.value?.plus(input)

            Action.MINUS ->
                _currentValue.value = _currentValue.value?.minus(input)
        }
        Log.d("ViewModel", "LiveData = ${currentValue.value}")
    }
}

enum class Action {
    PLUS,
    MINUS
}