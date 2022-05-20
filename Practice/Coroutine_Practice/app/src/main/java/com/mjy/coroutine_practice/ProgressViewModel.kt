package com.mjy.coroutine_practice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class ProgressViewModel : ViewModel(), Downloader {

    private val _progress = MutableLiveData<Int>(0)
    val progress: LiveData<Int>
        get() = _progress

    private var _enable = MutableLiveData<Boolean>(true)
    val enable = _enable

    fun runDownload() {
        CoroutineScope(Dispatchers.Main).launch {
            download()
        }
    }

    override suspend fun download() {
        _enable.value = false
        for (i in 1..100) {
            delay(100)
            _progress.value = i
        }
        _enable.value = true
    }
}