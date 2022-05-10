package com.example.training.fragments.blockFragment.blockViews

import android.content.Context
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.UiThreadTestRule
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.blockData.Range
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class BlockSetViewTest() {

    private lateinit var blockSetView: BlockSetView

    @UiThreadTest
    @Before
    fun createView() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        blockSetView = BlockSetView(context, { } )
    }

    @Test
    fun testAddBlockSet() {
        val blockSet = BlockSet(Range(10,12), Range(2,3))
        blockSetView.addBlockSet(blockSet)
        assertEquals(blockSet, blockSetView.getBlockSet())
    }

    @Test
    fun testIncorrectBlockSet() {
        val blockSet = BlockSet(Range(10,12), Range(2,3))
        blockSetView.addBlockSet(blockSet)

        val wrongBlockSet = BlockSet(Range(0,0), Range(0,0))
        assertNotEquals(wrongBlockSet, blockSetView.getBlockSet())
    }

    @Test
    fun testEqualsBlockSet() {
        val blockSet = BlockSet(Range(10,12), Range(2,3))
        blockSetView.addBlockSet(blockSet)
        assert(blockSetView.equalsBlockSet(blockSet))
    }

    @Test
    fun testWrongEqualsBlockSet() {
        val blockSet = BlockSet(Range(10,12), Range(2,3))
        blockSetView.addBlockSet(blockSet)

        val wrongBlockSet = BlockSet(Range(0,0), Range(0,0))
        assertFalse(blockSetView.equalsBlockSet(wrongBlockSet))
    }


}