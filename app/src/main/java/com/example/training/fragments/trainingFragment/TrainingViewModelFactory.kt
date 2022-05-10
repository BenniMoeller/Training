package com.example.training.fragments.trainingFragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.training.database.DatabaseDao


/**
 * viewModelFactory for the TrainingViewModel
 */
internal class TrainingViewModelFactory(private val dataSource: DatabaseDao, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_ cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainingViewModel::class.java)) {
            return TrainingViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}