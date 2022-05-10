package com.example.training.fragments.trainingFragment.trainingViews

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.blockData.Range
import com.example.training.database.dataClasses.trainingData.TrainingSet
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class TrainingSetViewTest {

    private lateinit var setView: TrainingSetView
    private val testBlockTrainingSet = BlockSet(Range(1, 1), Range(2,3))
    private val testTrainingSet = TrainingSet(100.0, 1, 13)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        setView = TrainingSetView(context, {})
    }
    //todo it seems like this view cant be tested. check if there aren't any test or delete this class
    @Test
    fun testAddInformation() {

    }


}