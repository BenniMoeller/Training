package com.example.training.fragments.bodyWeightFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.training.database.DatabaseDao
import com.example.training.database.dataClasses.BodyWeight
import com.example.training.repositories.BodyWeightRepository
import kotlinx.coroutines.*


/**
 * viewModel for the BodyWeightFragment
 */
internal class BodyWeightViewModel(private val database: DatabaseDao, application: Application) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val repository = BodyWeightRepository(database)

    val bodyWeights = repository.getAllBodyWeights() //all bodyWeights from the Database
    val bodyWeightString = MutableLiveData<String>() //the bodyWeightString that is also displayed in the ui with two way dataBinding


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * converts the bodyWeightString to a BodyWeight and saves it to the dataBase
     */
    fun saveBodyWeight() {
        uiScope.launch {
            withContext(Dispatchers.IO) { //todo make the input not save if it is in the wrong format
                val bodyWeight = bodyWeightString.value?.toDouble() ?: 0.0
                repository.saveBodyWeight(BodyWeight(bodyWeight))
                bodyWeightString.postValue("")
            }
        }
    }

    /**
     * deletes a bodyWeight from the dataBase
     * @param bodyWeight BodyWeight the bodyWeight to be deleted
     */
    fun deleteBodyWeight(bodyWeight: BodyWeight) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteBodyWeight(bodyWeight)
            }
        }
    }
}