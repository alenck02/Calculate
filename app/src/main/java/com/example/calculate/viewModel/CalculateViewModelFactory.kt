package com.example.calculate.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calculate.database.CalculateDao

class CalculateViewModelFactory(private val dao: CalculateDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalculateViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}