package com.example.calculate.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val expression: MutableLiveData<String> = MutableLiveData("")
}