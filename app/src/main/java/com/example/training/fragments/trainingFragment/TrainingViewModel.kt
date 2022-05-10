package com.example.training.fragments.trainingFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.training.database.DatabaseDao
import com.example.training.database.dataClasses.blockData.BlockExercise
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.blockData.BlockTrainingDay
import com.example.training.database.dataClasses.trainingData.TrainingDay
import com.example.training.database.dataClasses.trainingData.TrainingExercise
import com.example.training.database.dataClasses.trainingData.TrainingSet
import com.example.training.repositories.BlockRepository
import com.example.training.repositories.TrainingRepository
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException
import java.util.*


/**
 * viewmodel for the TrainingFragment
 */
internal class TrainingViewModel(private val database: DatabaseDao, application: Application) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val blockRepository = BlockRepository(database)
    private val trainingRepository = TrainingRepository(database)

    val currentBlockSets = MutableLiveData<List<BlockSet>>(mutableListOf())
    val currentTrainingSets = MutableLiveData<List<TrainingSet>>(mutableListOf())
    val isNewTrainingExercise = MutableLiveData(true)
    private lateinit var blockTrainingDay: BlockTrainingDay
    private var currentBlockExercise: BlockExercise? = null
    private var trainingDay: TrainingDay? = null
    private var weekIndex: Int = 0

    private val _trainingFinished = MutableLiveData<Boolean>(false)
    val trainingFinished: LiveData<Boolean>
        get() = _trainingFinished


    init {
        setUpTrainingTemplate()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * find the correct BlockTrainingDay from the database
     */
    private fun setUpTrainingTemplate() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val block = blockRepository.getCurrentBlock() ?: throw IllegalArgumentException() //change this to return to the start fragment
                blockTrainingDay = blockRepository.getBlockTrainingDay(block.id, Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2)
                                   ?: throw IllegalArgumentException("${block.id}, ${(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2)} ")
                //todo the -2 is because the calendar class stores the weekDay differently. get a value in a repository that returns the correct day value
                weekIndex = trainingRepository.getTodayWeekIndex(block.start)
                trainingDay = trainingRepository.getTrainingDay(blockTrainingDay.id, weekIndex)
                currentBlockExercise =
                    blockRepository.getBlockExercise(blockTrainingDay.id, 0) ?: throw IllegalArgumentException() //first get the first exercise
                getCurrentSets()
            }
        }
    }


    /**
     * saves the current BlockExercise that is displayed in the ui to the Database
     */
    fun saveTrainingExercise() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                if (trainingDay == null) { //create the TrainingDay for the first TrainingExercise that is saved
                    trainingDay = TrainingDay(weekIndex, blockTrainingDayId = blockTrainingDay.id)
                    trainingRepository.saveTrainingDay(trainingDay!!)
                }


                val trainingExercise: TrainingExercise?
                if (isNewTrainingExercise.value!!) {
                    trainingExercise = TrainingExercise(trainingDayId = trainingDay!!.id, blockTrainingExerciseId = currentBlockExercise!!.id)
                    trainingRepository.saveTrainingExercise(trainingExercise)
                } else {
                    trainingExercise = trainingRepository.getTrainingExercise(trainingDay!!.id, currentBlockExercise!!.id)
                }


                for (counter in currentTrainingSets.value!!.indices) {
                    val set = currentTrainingSets.value!![counter].apply {
                        blockSetId = currentBlockSets.value!![counter].id
                        trainingExerciseId = trainingExercise!!.id
                    }
                    trainingRepository.saveTrainingSet(set)
                }
                nextBlockTrainingExercise()
            }
        }
    }

    /**
     * switches to the next BlockTrainingExercise
     */
    private fun nextBlockTrainingExercise() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                currentBlockExercise = blockRepository.getBlockExercise(blockTrainingDay.id, currentBlockExercise!!.exerciseCounter + 1)
                if (currentBlockExercise != null) {
                    getCurrentSets()
                } else {
                    _trainingFinished.postValue(true)
                }
            }
        }
    }


    /**
     * retrieves the currentBlockSets and currentTrainingSets from the database
     */
    private fun getCurrentSets() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val blockSets = blockRepository.getTrainingSetsFromBlockExercise(currentBlockExercise!!.id)
                val trainingSets = trainingRepository.getTrainingSets(blockTrainingDay.id, weekIndex, currentBlockExercise!!.exerciseCounter)
                currentBlockSets.postValue(blockSets)
                currentTrainingSets.postValue(trainingSets)
                isNewTrainingExercise.postValue(trainingSets.isEmpty())
            }
        }
    }

    /**
     * resets the trainingFinished variable
     */
    fun resetTrainingFinished() {
        _trainingFinished.postValue(false)
    }
}