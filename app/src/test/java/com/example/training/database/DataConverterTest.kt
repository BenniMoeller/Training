package com.example.training.database

import org.junit.Assert.*

import org.junit.Test
import java.util.*

/**
 * class to test if dataconverter works correctly
 */
class DataConverterTest {
    private val dataConverter = DataConverter()


    @Test
    fun dateToValueTestAlreadyCorrectFormat() {
        val date = Date(1647903600000)
        val processedTime = dataConverter.dateToValue(date)
        assertEquals(date.time, processedTime)
    }

    @Test
    fun dateToValueTestIncorrectFormat() {
        val wantedTime = 1647903600000
        val date = Date(1647921585000)
        val processedTime = dataConverter.dateToValue(date)
        assertEquals(wantedTime, processedTime)
    }

    @Test
    fun dateToValueTestForWrongFormatChanged() {
        val date = Date(11768971512000)
        val processedTime = dataConverter.dateToValue(date)
        assertNotEquals(date.time, processedTime)
    }


}