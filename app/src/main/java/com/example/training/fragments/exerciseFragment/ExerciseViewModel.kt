package com.example.training.fragments.exerciseFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.training.database.DatabaseDao
import com.example.training.database.dataClasses.Exercise
import com.example.training.database.dataClasses.ExerciseType
import com.example.training.database.repositories.ExerciseRepository
import kotlinx.coroutines.*


/**
 * viewmodel for the ExerciseFragment
 */
internal class ExerciseViewModel(private val database: DatabaseDao, application: Application) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val repository = ExerciseRepository(database)

    val exercises = repository.getAllExercises()

    private val exerciseTypes = ExerciseType.values()
    val exerciseTypesAsString = exerciseTypes.map { it.name } //all the exerciseTypes displayed in spinner in the ui
    val selectedExerciseTypeIndex = MutableLiveData(0) //the index of the exerciseType selected in the ui
    val exerciseNameString = MutableLiveData("")

    private val _doesExerciseAlreadyExist = MutableLiveData<Boolean>(false)
    val doesExerciseAlreadyExist: LiveData<Boolean>
        get() = _doesExerciseAlreadyExist


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * saves the exercise to the database and resets the exerciseString and exerciseTypeIndex
     */
    fun saveExerciseToDatabase() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val exerciseType = exerciseTypes[selectedExerciseTypeIndex.value!!] //get the exerciseType selected in the ui
                val exercise = Exercise(exerciseNameString.value!!, exerciseType)
                try {
                    repository.saveExercise(exercise) //to check if an exercise with the same name already exist in the database
                } catch (exception: IllegalArgumentException) {
                    _doesExerciseAlreadyExist.postValue(true)
                }

                restoreExerciseState()
            }
        }
    }

    /**
     * resets the exerciseName and selectedExerciseTypeIndex
     */
    private fun restoreExerciseState() {
        exerciseNameString.postValue("")
        selectedExerciseTypeIndex.postValue(0)
    }


    /**
     * deletes an exercise from the database
     * @param exercise Exercise the exercise to be deleted
     */
    fun deleteExercise(exercise: Exercise) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteExercise(exercise)
            }
        }
    }


    /**
     * changes the mainLiftStatus of an exercise in the database
     * @param exercise Exercise the exercise which status should be changed
     */
    fun updateMainLift(exercise: Exercise) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                repository.changeMainLiftStatusOfExercise(exercise)
            }
        }
    }

    /**
     * resets the value of the doesExerciseAlreadyExist
     */
    fun resetDoesExerciseAlreadyExist() = _doesExerciseAlreadyExist.postValue(false)
}