package com.example.training.fragments.bodyWeightFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.training.database.repositories.BodyWeightRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


/**
 * viewmodel for the BodyWeightFragment
 */
internal class BodyWeightViewModel(private val repository: BodyWeightRepository, application: Application) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}