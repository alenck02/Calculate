package com.example.calculate.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculate.database.CalculateDao
import com.example.calculate.model.calculate
import kotlinx.coroutines.launch

class CalculateViewModel(private val dao: CalculateDao) : ViewModel() {
    val allCalculations: LiveData<List<calculate>> = dao.getAll()

    fun insert(calculation: calculate) {
        viewModelScope.launch {
            dao.insert(calculation)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            dao.deleteAll()
        }
    }
}