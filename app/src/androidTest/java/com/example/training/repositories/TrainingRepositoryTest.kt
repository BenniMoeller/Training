package com.example.training.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.training.database.TrainingDatabase
import com.example.training.database.dataClasses.Exercise
import com.example.training.database.dataClasses.ExerciseType
import com.example.training.database.dataClasses.blockData.*
import com.example.training.database.dataClasses.trainingData.TrainingDay
import com.example.training.database.dataClasses.trainingData.TrainingExercise
import com.example.training.database.dataClasses.trainingData.TrainingSet
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.IllegalArgumentException
import java.util.*

class TrainingRepositoryTest{

    private lateinit var exerciseRepository: ExerciseRepository
    private lateinit var trainingRepository: TrainingRepository
    private lateinit var dataBase: TrainingDatabase
    private lateinit var blockRepository: BlockRepository

    private val testExercise = Exercise("testExercise", ExerciseType.CALVES)
    private val testBlock = Block("test", true, Calendar.getInstance().time)
    private val testBlockTrainingDay = BlockTrainingDay(1,0)
    private val testBlockExercise = BlockExercise(1, testExercise.name, 0)
    private val testBlockSet1 = BlockSet(Range(1, 3), Range(4, 5), setIndex = 0, blockExerciseId = 1)
    private val testBlockSet2 = BlockSet(Range(10, 30), Range(40, 50), setIndex = 1, blockExerciseId = 1)
    private val testTrainingDay = TrainingDay(0, blockTrainingDayId = 1)
    private val testTrainingExercise = TrainingExercise(trainingDayId = 1, blockTrainingExerciseId = 1)
    private val testTrainingSet1 = TrainingSet(200.0, 1, 2, trainingExerciseId = 1, blockSetId = 1)
    private val testTrainingSet2 = TrainingSet(2000.0, 10, 20, trainingExerciseId = 1, blockSetId = 2)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dataBase = Room.inMemoryDatabaseBuilder(context, TrainingDatabase::class.java).build()
        trainingRepository = TrainingRepository(dataBase.databaseDao)
        blockRepository = BlockRepository(dataBase.databaseDao)
        exerciseRepository = ExerciseRepository(dataBase.databaseDao)

        exerciseRepository.saveExercise(testExercise)
        blockRepository.saveBlock(testBlock)
        blockRepository.saveBlockTrainingDay(testBlockTrainingDay)
        blockRepository.saveBlockExercise(testBlockExercise)
        blockRepository.saveBlockSet(testBlockSet1)
        blockRepository.saveBlockSet(testBlockSet2)
    }

    @After
    fun tearDown() {
        dataBase.close()
    }

    @Test
    fun testSaveAndRetrieveTrainingDay() {
        trainingRepository.saveTrainingDay(testTrainingDay)
        val trainingDay = trainingRepository.getTrainingDay(testBlockTrainingDay.id, testTrainingDay.weekIndex)
        assertEquals(testTrainingDay, trainingDay)

        assertTrue(testTrainingDay.id == 1L)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaveIncorrectTrainingDay() {
        val wrongTrainingDay = TrainingDay(10, blockTrainingDayId = 20)
        trainingRepository.saveTrainingDay(wrongTrainingDay)
    }

    @Test
    fun testSaveAndRetrieveTrainingExercise() {
        trainingRepository.saveTrainingDay(testTrainingDay)
        trainingRepository.saveTrainingExercise(testTrainingExercise)
        val trainingExercise = trainingRepository.getTrainingExercise(testTrainingDay.id, testBlockExercise.id)
        assertEquals(testTrainingExercise, trainingExercise)

        assertTrue(testTrainingExercise.id == 1L)

    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaveIncorrectTrainingExercise() {
        val wrongTrainingExercise = TrainingExercise(trainingDayId = 10L, blockTrainingExerciseId = 30L)
        trainingRepository.saveTrainingExercise(wrongTrainingExercise)
    }

    @Test
    fun testSaveAndRetrieveBlockSets() {
        trainingRepository.saveTrainingDay(testTrainingDay)
        trainingRepository.saveTrainingExercise(testTrainingExercise)
        trainingRepository.saveTrainingSet(testTrainingSet1)
        trainingRepository.saveTrainingSet(testTrainingSet2)

        assertTrue(testTrainingSet1.id == 1L)
        assertTrue(testTrainingSet2.id == 2L)

        val trainingSets = trainingRepository.getTrainingSets(testBlockTrainingDay.id, testTrainingDay.weekIndex, testBlockExercise.exerciseCounter)
        val expectedList = listOf(testTrainingSet1, testTrainingSet2)
        assertEquals(expectedList, trainingSets)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaveIncorrectTrainingSet() {
        val wrongTrainingSet = TrainingSet(100.0, 10, 10, trainingExerciseId = 100, blockSetId = 100)
        trainingRepository.saveTrainingSet(wrongTrainingSet)
    }

    @Test
    fun testGetTodayWeekIndex() {
        assertTrue(trainingRepository.getTodayWeekIndex(Calendar.getInstance().time) == 0)

        val twoWeeks = Calendar.getInstance().apply { set(Calendar.WEEK_OF_YEAR, this.get(Calendar.WEEK_OF_YEAR) + 2)}
        assertTrue(trainingRepository.getTodayWeekIndex(twoWeeks.time) == 2)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testWrongGetTodayWeekIndex() {
        val oneWeekAgo = Calendar.getInstance().apply { set(Calendar.WEEK_OF_YEAR, this.get(Calendar.WEEK_OF_YEAR) - 1) }
        trainingRepository.getTodayWeekIndex(oneWeekAgo.time)
    }


}