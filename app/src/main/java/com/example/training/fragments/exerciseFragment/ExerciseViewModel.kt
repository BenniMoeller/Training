package com.example.training.fragments.exerciseFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.training.database.repositories.ExerciseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


/**
 * viewmodel for the ExerciseFragment
 */
internal class ExerciseViewModel(private val repository: ExerciseRepository, application: Application) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}