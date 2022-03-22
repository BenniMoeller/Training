package com.example.training.fragments.bodyWeightFragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.training.database.repositories.BodyWeightRepository


/**
 * viewModelFactory for the BodyWeightViewModel
 */
internal class BodyWeightViewModelFactory(
    private val dataSource: BodyWeightRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_ cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BodyWeightViewModel::class.java)) {
            return BodyWeightViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}