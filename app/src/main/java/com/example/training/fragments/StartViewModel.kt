package com.example.training.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.training.database.DatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


/**
 * viewmodel for the StartFragment
 */
internal class StartViewModel(private val dataBase: DatabaseDao, application: Application) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}