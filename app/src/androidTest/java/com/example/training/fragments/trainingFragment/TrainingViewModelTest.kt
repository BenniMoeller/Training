package com.example.training.fragments.trainingFragment

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import com.example.training.database.TrainingDatabase
import com.example.training.database.dataClasses.Exercise
import com.example.training.database.dataClasses.ExerciseType
import com.example.training.database.dataClasses.blockData.*
import com.example.training.database.dataClasses.trainingData.TrainingDay
import com.example.training.database.dataClasses.trainingData.TrainingExercise
import com.example.training.database.dataClasses.trainingData.TrainingSet
import com.example.training.repositories.BlockRepository
import com.example.training.repositories.ExerciseRepository
import com.example.training.repositories.TrainingRepository
import kotlinx.coroutines.delay
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

class TrainingViewModelTest {
    private lateinit var dataBase: TrainingDatabase
    private lateinit var blockRepository: BlockRepository
    private lateinit var exerciseRepository: ExerciseRepository
    private lateinit var trainingRepository: TrainingRepository
    private lateinit var trainingViewModel: TrainingViewModel

    private val testExercise = Exercise("testExercise", ExerciseType.CALVES)
    private val testBlock = Block("testBlock", true, Calendar.getInstance().time)
    private val testBlockTrainingDay = BlockTrainingDay(1,0)
    private val testBlockExercise1 = BlockExercise(1,testExercise.name, 0)
    private val testBlockExercise2 = BlockExercise(1,testExercise.name, 1)
    private val testBlockSet1 = BlockSet(Range(1, 2), Range(3, 4), setIndex = 0, blockExerciseId = 1)
    private val testBlockSet2 = BlockSet(Range(10, 20), Range(30, 40), setIndex = 1, blockExerciseId = 1)
    private val testBlockSet3 = BlockSet(Range(6, 7), Range(8, 9), setIndex = 0, blockExerciseId = 2)
    private val testBlockSet4 = BlockSet(Range(60, 70), Range(80, 90), setIndex = 1, blockExerciseId = 2)
    private val testTrainingDay = TrainingDay(0, blockTrainingDayId = 1)
    private val testTrainingExercise1 = TrainingExercise(trainingDayId = 1, blockTrainingExerciseId = 1)
    private val testTrainingExercise2 = TrainingExercise(trainingDayId = 1, blockTrainingExerciseId = 2)
    private val testTrainingSet1 = TrainingSet(1.0, 1, 2, trainingExerciseId = 1, blockSetId = 1)
    private val testTrainingSet2 = TrainingSet(10.0, 10, 20, trainingExerciseId = 1, blockSetId = 2)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        dataBase = Room.inMemoryDatabaseBuilder(context, TrainingDatabase::class.java).build()
        blockRepository = BlockRepository(dataBase.databaseDao)
        exerciseRepository = ExerciseRepository(dataBase.databaseDao)
        trainingRepository = TrainingRepository(dataBase.databaseDao)

        exerciseRepository.saveExercise(testExercise)
        blockRepository.saveBlock(testBlock)
        testBlockTrainingDay.dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2 //todo change this when a function for this exists
        blockRepository.saveBlockTrainingDay(testBlockTrainingDay)
        blockRepository.saveBlockExercise(testBlockExercise1)
        blockRepository.saveBlockExercise(testBlockExercise2)
        blockRepository.saveBlockSet(testBlockSet1)
        blockRepository.saveBlockSet(testBlockSet2)
        blockRepository.saveBlockSet(testBlockSet3)
        blockRepository.saveBlockSet(testBlockSet4)

        trainingViewModel = TrainingViewModel(dataBase.databaseDao, context)
        Thread.sleep(500)


    }


    @After
    fun tearDown() {
        dataBase.close()
    }


    @Test
    fun testGetCorrectBlockSetsAndTrainingSets() {
        val currentBlockSets = trainingViewModel.currentBlockSets.value
        val currentTrainingSets = trainingViewModel.currentTrainingSets.value
        assertEquals(listOf(testBlockSet1, testBlockSet2), currentBlockSets)
        assertEquals(emptyList<TrainingSet>(), currentTrainingSets)
    }


    @Test
    fun testSavingTrainingSets() {
        trainingViewModel.currentTrainingSets.postValue(listOf(testTrainingSet1, testTrainingSet2))
        trainingViewModel.saveTrainingExercise()
        Thread.sleep(500)
        val retrievedSets = trainingRepository.getTrainingSets(testBlockTrainingDay.id, 0,0)
        Thread.sleep(500)
        assertEquals(listOf(testTrainingSet1, testTrainingSet2), retrievedSets)
    }


    @Test
    fun testTrainingSetAsTemplate() {
        trainingViewModel.currentTrainingSets.postValue(listOf(testTrainingSet1, testTrainingSet2))
        trainingViewModel.saveTrainingExercise()

        //reset trainingViewModel by getting a new one
        val context = ApplicationProvider.getApplicationContext<Application>()
        trainingViewModel = TrainingViewModel(dataBase.databaseDao, context)

        Thread.sleep(500)
        //now check if the trainingSets are correctly retrieved
        assertEquals(listOf(testTrainingSet1, testTrainingSet2), trainingViewModel.currentTrainingSets.value!!)
    }
}