package com.example.training.fragments.blockFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.training.database.DatabaseDao
import com.example.training.database.dataClasses.blockData.Block
import com.example.training.database.dataClasses.blockData.BlockExercise
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.blockData.BlockTrainingDay
import com.example.training.repositories.BlockRepository
import com.example.training.repositories.ExerciseRepository
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException
import java.util.*


/**
 * viewmodel for the BlockFragment
 */
internal class BlockViewModel(private val database: DatabaseDao, application: Application) : AndroidViewModel(application) {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val blockRepository = BlockRepository(database)
    var startDate: Date = Calendar.getInstance().time //the startDate of the Block
    val exercises = ExerciseRepository(database).getAllExerciseNames()
    private var weekdays = listOf<Int>() //the weekdays on which training days are planned

    // list for all the BlockSets that are meant to be in this block.
    // first dimension is for the BlockTrainingDay, the second for an BlockExercise and the third are the blockSets
    private val savedBlockSets: MutableList<MutableList<MutableList<BlockSet>>> = mutableListOf()

    //list for the BlockExercises that are meant to be in this block
    //the first dimension is for the BlockTrainingDay and the second one for the index of the exercises themselves
    private val savedExercises: MutableList<MutableList<Int>> = mutableListOf()

    private val _blockSaved = MutableLiveData<Boolean>(false)
    val blockSaved: LiveData<Boolean>
        get() = _blockSaved


    private var currentWeekDayIndex = 0 //the index of the  weekday that is currently selected/ being worked on
    private var currentBlockExerciseIndex = 0 //the index of the BlockExercise that is currently selected/ being worked on

    val displayedBlockSets = MutableLiveData<MutableList<BlockSet>>(mutableListOf()) //the BlockSets that are displayed atm in the ui
    val displayedExerciseIndex = MutableLiveData(0) //the index of the exercise that is selected in the spinner in the xml
    val blockExerciseIndexString = MutableLiveData<String>() //displays the index of the blockExercise as a string


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * updates the displayed BlockExercise so that the current BlockExercise and the associated BlockSets are displayed in the ui
     */
    private fun displayBlockExercise() {
        checkDataForValidity()
        val newSets = savedBlockSets[currentWeekDayIndex][currentBlockExerciseIndex]
        val newExerciseIndex = savedExercises[currentWeekDayIndex][currentBlockExerciseIndex]
        displayedBlockSets.postValue(newSets)
        displayedExerciseIndex.postValue(newExerciseIndex)
        updateDisplayedBlockExerciseIndex()
    }


    /**
     * saves the displayed BlockExercise to the savedExercises and savedBlockSets variables
     */
    private fun saveBlockExercise() {
        when {
            currentBlockExerciseIndex < savedBlockSets[currentWeekDayIndex].size  -> { //a saved list of BlockSets and Exercises already exists
                savedBlockSets[currentWeekDayIndex][currentBlockExerciseIndex] = displayedBlockSets.value!!
                savedExercises[currentWeekDayIndex][currentBlockExerciseIndex] = displayedExerciseIndex.value!!
            }
            currentBlockExerciseIndex == savedBlockSets[currentWeekDayIndex].size -> { //this is a new blockExercise that has yet to be saved
                savedBlockSets[currentWeekDayIndex].add(displayedBlockSets.value!!)
                savedExercises[currentWeekDayIndex].add(displayedExerciseIndex.value!!)
            }
            else                                                                  -> throw IllegalArgumentException("The currentBlockExerciseIndex is too high")
        }
    }

    /**
     * checks whether the current BlockExercise is the last one and if not adds one to the savedBlockSets and savedExercises
     */
    private fun checkForNextBlockExercise() {
        if (currentBlockExerciseIndex >= savedBlockSets[currentWeekDayIndex].size) //if we're at the last BlockExercise in this day we add a new one
        {
            savedBlockSets[currentWeekDayIndex].add(mutableListOf())
            savedExercises[currentWeekDayIndex].add(0)
        }
    }

    /**
     * changes to the next BlockExercise
     */
    fun nextBlockExercise() {
        saveBlockExercise()
        currentBlockExerciseIndex++
        checkForNextBlockExercise()
        displayBlockExercise()
    } //todo add the feature to delete a BlockExercise


    /**
     * changes to the previous blockExercise if possible
     */
    fun previousBlockExercise() {
        saveBlockExercise()
        if (currentBlockExerciseIndex > 0) currentBlockExerciseIndex--
        displayBlockExercise()
        //todo add animation when swiping through the BlockExercises
    }

    /**
     * updates the blockExerciseIndexString so it displayes a new correct index of the BlockExercise
     */
    private fun updateDisplayedBlockExerciseIndex() {
        val amountBlockExercises = savedBlockSets[currentWeekDayIndex].size
        val newBlockExerciseIndexString = "${currentBlockExerciseIndex + 1}/$amountBlockExercises"
        blockExerciseIndexString.postValue(newBlockExerciseIndexString)
    }


    /**
     * sets a new weekday that is currently worked on
     * @param position Int
     */
    fun setNewWeekDay(position: Int) {
        if (position >= weekdays.size) throw IllegalArgumentException("Position must not be greater than the amount of selected weekdays")
        saveBlockExercise()
        currentWeekDayIndex = position
        currentBlockExerciseIndex = 0 //since we don't know how many blockExercises the new day has
        checkForNextBlockExercise()
        displayBlockExercise()
    }

    /**
     * adds the weekDays on which TrainingDays are planned
     * @param newWeekDays List<Int> list containing the index of the weekdays on which training is planned. eg 0 for Monday, 1 for Tuesday etc.
     */
    fun addWeekDays(newWeekDays: List<Int>) {
        weekdays = newWeekDays //since we now have the amount of trainingDays we can also set the first dimension of the sets and exercises
        savedBlockSets.apply { this.clear(); addAll(MutableList(weekdays.size) { mutableListOf() }) }
        savedExercises.apply { this.clear(); addAll(MutableList(weekdays.size) { mutableListOf() }) }
    }

    /**
     * checks the saved data for validity and throws assertion if they are not correct
     */
    private fun checkDataForValidity() {
        assert(savedBlockSets.size == savedExercises.size,
               { "There should be as many days for the exercises as for the BlockSets" }) //check if same amount of exercises and blockSets are saved
        for (index in savedBlockSets.indices) {
            assert(savedBlockSets[index].size == savedExercises[index].size, { "There aren't the same amount of exercises and BlockExercises" })
        }

        assert(currentWeekDayIndex < savedBlockSets.size, { "There is no saved data  for the selected Weekday" })
        assert(currentBlockExerciseIndex < savedBlockSets[currentWeekDayIndex].size, { "There is no saved data for the selected BlockExercise" })
    }

    /**
     * saves the block that is currently displayed in the ViewModel/Ui to the database
     */
    fun saveBlock(blockName: String, isDevelopmentBlock: Boolean) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val block = Block(blockName, isDevelopmentBlock, startDate)
                blockRepository.saveBlock(block)
                for (index in savedBlockSets.indices) {
                    saveBlockTrainingDay(block.id, weekdays[index], index)
                }
            }
        }
        _blockSaved.postValue(true)
        //todo add test classes for all relevant classes. also test for the ui not just for the classes themselves
    }

    /**
     * saves a BlockTrainingDay to the database
     * @param blockId Long the of the Block this BlockTrainingDay belongs to
     * @param dayOfWeek Int the dayOfTheWeek this BlockTrainingDay takes place in. eg 1 for Monday
     * @param dayIndex Int the index describing the how manyth BlockTrainingDay this is in the week
     */
    private fun saveBlockTrainingDay(blockId: Long, dayOfWeek: Int, dayIndex: Int) {
        val blockTrainingDay = BlockTrainingDay(blockId, dayOfWeek)
        blockRepository.saveBlockTrainingDay(blockTrainingDay)

        savedBlockSets[dayIndex].forEachIndexed { index, blockExercise -> //now to save the BlockExercises
            val exerciseName = exercises.value!![savedExercises[dayIndex][index]]
            val blockSets = savedBlockSets[dayIndex][index]
            saveBlockExercise(blockTrainingDay.id, index, exerciseName, blockSets)
        }
    }

    /**
     * saves a BlockExercise to the dataBase
     * @param blockTrainingDayId Long the of the BlockTrainingDay this BlockExercise belongs to
     * @param exerciseName String the name of the Exercise that is done for this BlockExercise
     * @param blockSetList List<BlockSet> the BlockSets that are taking place in this BlockSet
     */
    private fun saveBlockExercise(blockTrainingDayId: Long, exerciseIndex: Int, exerciseName: String, blockSetList: List<BlockSet>) {
        val blockExercise = BlockExercise(blockTrainingDayId, exerciseName, exerciseIndex)
        blockRepository.saveBlockExercise(blockExercise)
        blockSetList.forEachIndexed { index, blockSet ->
            saveBlockSet(blockSet, blockExercise.id, index)
        }
    }

    /**
     * saves a BlockSet to the database. can only be called from within a thread that isn't the main thread
     * @param blockSet BlockSet the BlockSet to be saved
     * @param blockExerciseId Long the id of the BlockExercise this BlockSet belongs to
     * @param setIndex Int the index of the BlockSet in this BlockExercise
     */
    private fun saveBlockSet(blockSet: BlockSet, blockExerciseId: Long, setIndex: Int) {
        blockSet.blockExerciseId = blockExerciseId
        blockSet.setIndex = setIndex
        blockRepository.saveBlockSet(blockSet)
    }

    /**
     * resets the blockSaved variable to false
     */
    fun resetBlockSaved() {
        _blockSaved.postValue(false)
    }


}