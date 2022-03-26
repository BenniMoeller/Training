package com.example.training.database

import com.example.training.database.dataClasses.blockData.Range
import org.junit.Assert.*

import org.junit.Test
import java.util.*

/**
 * class to test if dataconverter works correctly
 */
class DataConverterTest {
    private val dataConverter = DataConverter()


    @Test
    fun testDateToValueTestAlreadyCorrectFormat() {
        val date = Date(1647903600000)
        val processedTime = dataConverter.dateToValue(date)
        assertEquals(date.time, processedTime)
    }

    @Test
    fun testDateToValueTestIncorrectFormat() {
        val wantedTime = 1647903600000
        val date = Date(1647921585000)
        val processedTime = dataConverter.dateToValue(date)
        assertEquals(wantedTime, processedTime)
    }

    @Test
    fun testDateToValueTestForWrongFormatChanged() {
        val date = Date(11768971512000)
        val processedTime = dataConverter.dateToValue(date)
        assertNotEquals(date.time, processedTime)
    }

    @Test
    fun testRangeToStringToRange() {
        val range = Range(1, 10)
        val rangeString = dataConverter.rangeToValue(range)
        val convertedRange = dataConverter.valueToRange(rangeString)

        assertEquals(range, convertedRange)
    }


}