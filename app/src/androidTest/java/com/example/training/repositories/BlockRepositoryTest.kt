package com.example.training.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.training.database.TrainingDatabase
import com.example.training.database.dataClasses.Exercise
import com.example.training.database.dataClasses.ExerciseType
import com.example.training.database.dataClasses.blockData.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

class BlockRepositoryTest() {
    private lateinit var blockRepository: BlockRepository
    private lateinit var exerciseRepository: ExerciseRepository
    private lateinit var database: TrainingDatabase

    private val testBlock = Block("test", true, Calendar.getInstance().time)
    private val testBlockTrainingDay = BlockTrainingDay(1, 0)
    private val testExercise = Exercise("test", ExerciseType.CALVES)
    private val testBlockExercise = BlockExercise(1, "test", 0)
    private val testBlockSet = BlockSet(Range(0, 0), Range(0, 0)).apply { blockExerciseId = 1L }

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TrainingDatabase::class.java).build()
        blockRepository = BlockRepository(database.databaseDao)
        exerciseRepository = ExerciseRepository(database.databaseDao)
        fillDatabaseWithData()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testSaveBlock() {
        val block1 = Block("testname", true, Calendar.getInstance().time)
        val block2 = Block("testname2", false, Calendar.getInstance().time)
        blockRepository.saveBlock(block1)
        blockRepository.saveBlock(block2)
        assert(block1.id == 2L) //since we already have a block in the dataBase thanks to fillDatabaseWithData()
        assert(block2.id == 3L)
        //todo once the BlockRepository has the functionality  to get a Block by the id, also test here if you can retrieve it
    }

    @Test
    fun testSaveBlockTrainingDay() {
        val blockTrainingDay = BlockTrainingDay(testBlock.id, 1)
        blockRepository.saveBlockTrainingDay(blockTrainingDay)
        assert(blockTrainingDay.id == 2L) //since there is already a blockTrainingDay in the database thanks to fillDatabaseWithDate()
        //todo once the BlockRepository has the ability to retrieve BlockTrainingDays also test this here
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaveBlockTrainingDayTwice() {
        val blockTrainingDay = BlockTrainingDay(testBlock.id, 1)
        blockRepository.saveBlockTrainingDay(blockTrainingDay)
        blockRepository.saveBlockTrainingDay(blockTrainingDay)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaveBlockTrainingDayWithIncorrectBlock() {
        val blockTrainingDay = BlockTrainingDay(10L, 5)
        blockRepository.saveBlockTrainingDay(blockTrainingDay)
    }

    @Test
    fun saveBlockExercise() { //first we have to save a block, a blockTrainingDay and a exercise
        val blockExercise = BlockExercise(testBlockTrainingDay.id, testExercise.name, 10)
        blockRepository.saveBlockExercise(blockExercise)
        assert(blockExercise.id == 2L) //since there is already a BlockExercise in the database thanks to fillDatabaseWithData()
        //todo once the blockRepository has the ability to retrieve a BlockExercise also test this here
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaveBlockExerciseTwice() {
        val blockExercise = BlockExercise(testBlockTrainingDay.id, testExercise.name, 0)
        blockRepository.saveBlockExercise(blockExercise)
        blockRepository.saveBlockExercise(blockExercise)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaveBlockExerciseIncorrectExercise() {
        val blockExercise = BlockExercise(testBlockTrainingDay.id, "wrongValue", 0)
        blockRepository.saveBlockExercise(blockExercise)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaveBlockExerciseIncorrectBlockTrainingDay() {
        val blockExercise = BlockExercise(10L, testExercise.name, 0)
        blockRepository.saveBlockExercise(blockExercise)
    }

    @Test
    fun saveBlockSet() {
        val blockSet = BlockSet(Range(10, 10), Range(10, 10)).apply {
            blockExerciseId = testBlockExercise.id
            setIndex = 2
        }
        blockRepository.saveBlockSet(blockSet)
        assert(blockSet.id == 2L) //since there is already a BlockSet in the database thanks to fillDatabaseWithData()
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaveBlockSetTwice() {
        val blockSet = BlockSet(Range(10, 20), Range(4, 6))
        blockSet.blockExerciseId = testBlockExercise.id
        blockRepository.saveBlockSet(blockSet)
        blockRepository.saveBlockSet(blockSet)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaveBlockSetIllegalBlockExercise() {
        val blockSet = BlockSet(Range(5, 4), Range(3, 4))
        blockRepository.saveBlockSet(blockSet)
    }

    /**
     * fills the database with some data so we can always do it like this easily
     */
    private fun fillDatabaseWithData() {
        blockRepository.saveBlock(testBlock)
        blockRepository.saveBlockTrainingDay(testBlockTrainingDay)
        exerciseRepository.saveExercise(testExercise)
        blockRepository.saveBlockExercise(testBlockExercise)
        blockRepository.saveBlockSet(testBlockSet)
    }
}