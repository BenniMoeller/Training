package com.example.training.fragments.trainingFragment.trainingViews

import android.content.Context
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.blockData.Range
import com.example.training.database.dataClasses.trainingData.TrainingSet
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class TrainingExerciseViewTest {

    private lateinit var exerciseView: TrainingExerciseView
    private val testBlockList = mutableListOf(BlockSet(Range(1, 1), Range(3, 3)), BlockSet(Range(5, 5), Range(2, 3)))
    private val testTrainingList = mutableListOf(TrainingSet(100.0, 1, 3), TrainingSet(80.0, 5, 3))

    @UiThreadTest
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        exerciseView = TrainingExerciseView(context)
    }

    @UiThreadTest
    @Test
    fun testAddSetList() {
        exerciseView.addSetList(testBlockList, true, testTrainingList)
        assert(exerciseView.getTrainingSetList().size == testBlockList.size)
    }

    @UiThreadTest
    @Test(expected = IllegalArgumentException::class)
    fun testAddIncorrectSetList() {
        exerciseView.addSetList(testBlockList, true, testTrainingList + TrainingSet(50.0, 10, 3))
    }

    @UiThreadTest
    @Test
    fun testGetEmptyTrainingSetList() {
        assert(exerciseView.getTrainingSetList().isEmpty())
    }

    @UiThreadTest
    @Test
    fun testEqualsTrainingSetList() {
        assert(exerciseView.equalTrainingSetList(mutableListOf()))

        exerciseView.addSetList(testBlockList, true,  testTrainingList)
        val expectedList = List(testTrainingList.size) { TrainingSet(0.0, 0, 0) }
        assert(exerciseView.equalTrainingSetList(expectedList))
    }

    @UiThreadTest
    @Test
    fun testEqualsTrainingSetListIncorrectSize() {
        exerciseView.addSetList(testBlockList, true, testTrainingList)
        val wrongList = testTrainingList + TrainingSet(200.0, 0, 0)
        assertFalse(exerciseView.equalTrainingSetList(wrongList))
    }


}