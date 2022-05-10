package com.example.training.fragments.blockFragment.blockViews

import android.content.Context
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import com.example.training.database.dataClasses.blockData.Block
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.blockData.Range
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BlockExerciseViewTest() {
    private lateinit var blockExerciseView: BlockExerciseView
    private val setList = listOf(BlockSet(Range(10, 12), Range(4, 5)), BlockSet(Range(4, 5), Range(7, 8)))
    private val wrongSetList = listOf(BlockSet(Range(0,0), Range(0,0)))

    @UiThreadTest
    @Before
    fun createView() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        blockExerciseView = BlockExerciseView(context)
    }

    @UiThreadTest
    @Test
    fun testAddBlockSets() {
        blockExerciseView.addBlockSets(setList)
        assertEquals(blockExerciseView.getBlockSets(), setList)
    }

    @UiThreadTest
    @Test
    fun testEqualsSetList() {
        blockExerciseView.addBlockSets(setList)
        assert(blockExerciseView.equalsBlockSets(setList))
    }

    @UiThreadTest
    @Test
    fun testEqualsSetListWrong() {
        blockExerciseView.addBlockSets(setList)
        assertFalse(blockExerciseView.equalsBlockSets(wrongSetList))

        val longerList = setList + BlockSet(Range(0,0), Range(0,0))
        blockExerciseView.addBlockSets(longerList)
        assertFalse(blockExerciseView.equalsBlockSets(setList))
    }
}
