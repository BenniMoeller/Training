package com.example.training.fragments.blockFragment

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import com.example.training.database.TrainingDatabase
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.blockData.Range
import com.example.training.repositories.BlockRepository
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class BlockViewModelTest {
    private lateinit var blockRepository: BlockRepository
    private lateinit var viewModel: BlockViewModel
    private val testList1 = mutableListOf(BlockSet(Range(1, 1), Range(2, 2)))
    private val testList2 = mutableListOf(BlockSet(Range(3, 3), Range(4, 4)))
    private val testExercise1 = 5
    private val testExercise2 = 10


    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val database = Room.inMemoryDatabaseBuilder(context, TrainingDatabase::class.java).build()
        viewModel = BlockViewModel(database.databaseDao, context)
        viewModel.addWeekDays(mutableListOf(1, 5, 7))
        blockRepository = BlockRepository(database.databaseDao)
    }

    @UiThreadTest
    @Test
    fun testBlockExerciseSaved() {
        viewModel.displayedBlockSets.value = testList1
        viewModel.displayedExerciseIndex.value = testExercise1
        viewModel.nextBlockExercise()
        viewModel.previousBlockExercise()
        assertEquals(viewModel.displayedBlockSets.value, testList1)
        assertEquals(viewModel.displayedExerciseIndex.value, testExercise1)
    }

    @UiThreadTest
    @Test
    fun saveMultipleBlockSetExercises() {
        viewModel.displayedBlockSets.value = testList1
        viewModel.displayedExerciseIndex.value = testExercise1
        viewModel.nextBlockExercise()
        viewModel.displayedBlockSets.value = testList2
        viewModel.displayedExerciseIndex.value = testExercise2
        viewModel.nextBlockExercise()


        viewModel.previousBlockExercise()
        assertEquals(viewModel.displayedBlockSets.value, testList2)
        assertEquals(viewModel.displayedExerciseIndex.value, testExercise2)
        viewModel.previousBlockExercise()
        assertEquals(viewModel.displayedBlockSets.value, testList1)
        assertEquals(viewModel.displayedExerciseIndex.value, testExercise1)
    }

    @UiThreadTest
    @Test
    fun previousOnFirstBlockExerciseNotPossible() {
        viewModel.displayedBlockSets.value = testList1
        viewModel.displayedExerciseIndex.value = testExercise1
        viewModel.previousBlockExercise()
        assertEquals(viewModel.displayedBlockSets.value, testList1)
        assertEquals(viewModel.displayedExerciseIndex.value, testExercise1)
    }

    @UiThreadTest
    @Test
    fun createNewBlockExercise() {
        viewModel.displayedBlockSets.value = testList1
        viewModel.displayedExerciseIndex.value = testExercise1
        viewModel.nextBlockExercise()
        assertEquals(viewModel.displayedBlockSets.value, mutableListOf<BlockSet>())
        assertEquals(viewModel.displayedExerciseIndex.value, 0)
    }

    @UiThreadTest
    @Test
    fun changeWeekDay() {
        viewModel.displayedBlockSets.value = testList1
        viewModel.displayedExerciseIndex.value = testExercise1
        viewModel.setNewWeekDay(1)
        assertEquals(viewModel.displayedBlockSets.value, mutableListOf<BlockSet>())
        assertEquals(viewModel.displayedExerciseIndex.value, 0)
    }

    @UiThreadTest
    @Test
    fun saveDataOnWeekDayChange() {
        viewModel.nextBlockExercise()
        viewModel.displayedBlockSets.value = testList1
        viewModel.displayedExerciseIndex.value = testExercise1

        viewModel.setNewWeekDay(1)
        viewModel.setNewWeekDay(0)
        viewModel.nextBlockExercise()
        assertEquals(viewModel.displayedBlockSets.value, testList1)
        assertEquals(viewModel.displayedExerciseIndex.value, testExercise1)
    }

    @UiThreadTest
    @Test
    fun testBlockExerciseIndexString() {
        assertEquals(viewModel.blockExerciseIndexString.value, "1/1")
        viewModel.nextBlockExercise()
        assertEquals(viewModel.blockExerciseIndexString.value, "2/2")
        viewModel.nextBlockExercise()
        assertEquals(viewModel.blockExerciseIndexString.value, "3/3")
        viewModel.previousBlockExercise()
        assertEquals(viewModel.blockExerciseIndexString.value, "2/3")
        viewModel.previousBlockExercise()
        assertEquals(viewModel.blockExerciseIndexString.value, "1/3")
        viewModel.previousBlockExercise()
        assertEquals(viewModel.blockExerciseIndexString.value, "1/3")
    }

    @UiThreadTest
    @Test
    fun testBlockExerciseIndexStringOnWeekDayChange() {
        viewModel.nextBlockExercise()
        viewModel.nextBlockExercise()
        assertEquals(viewModel.blockExerciseIndexString.value, "3/3")
        viewModel.setNewWeekDay(2)
        assertEquals(viewModel.blockExerciseIndexString.value, "1/1")
        viewModel.setNewWeekDay(0)
        assertEquals(viewModel.blockExerciseIndexString.value, "1/3")
    }

    @UiThreadTest
    @Test
    fun testSaveBlock() {
        viewModel.displayedBlockSets.value = (testList1)
        viewModel.saveBlock("test", true)

        //todo finish this test when the blockrepository has the functionality to retrieve block elements. also finish the next two tests
    }

    @UiThreadTest
    @Test
    fun testSaveBlockAfterNextBlockExercise() {

    }

    @UiThreadTest
    @Test
    fun testSaveBlockOnDifferentWeekday() {

    }


}