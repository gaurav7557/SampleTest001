package com.example.testapplication002.ui.main.viewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val message = MutableLiveData<String>()

    fun process(text: String) {
        message.value = text
    }
}