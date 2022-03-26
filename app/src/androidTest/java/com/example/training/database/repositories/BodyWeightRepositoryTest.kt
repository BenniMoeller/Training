package com.example.training.database.repositories

import android.content.Context
import androidx.lifecycle.LifecycleRegistry
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.training.database.DatabaseDao
import com.example.training.database.TrainingDatabase
import com.example.training.database.dataClasses.BodyWeight
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

class BodyWeightRepositoryTest {

    private lateinit var bodyWeightRepository: BodyWeightRepository
    private lateinit var database: TrainingDatabase

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, TrainingDatabase::class.java).build()
        bodyWeightRepository = BodyWeightRepository(database.databaseDao)
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun testSaveAndGetByIdBodyWeight() {
        val bodyWeight1 = BodyWeight(90.0, Calendar.getInstance().time)
        val bodyWeight2 = BodyWeight(100.0, Calendar.getInstance().time)
        bodyWeightRepository.saveBodyWeight(bodyWeight1)
        bodyWeightRepository.saveBodyWeight(bodyWeight2)
        assertTrue(bodyWeight1.id == 1L)
        assertTrue(bodyWeight2.id == 2L)

        val storedBodyWeight1 = bodyWeightRepository.getBodyWeightById(bodyWeight1.id)
        assertTrue(storedBodyWeight1.weight == bodyWeight1.weight)
        assertTrue(storedBodyWeight1.date == bodyWeight1.date)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testIncorrectGetById() {
        bodyWeightRepository.getBodyWeightById(100L)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testDeleteBodyWeight() {
        val bodyWeight = BodyWeight(50.0, Calendar.getInstance().time)
        bodyWeightRepository.saveBodyWeight(bodyWeight)
        bodyWeightRepository.deleteBodyWeight(bodyWeight)
        bodyWeightRepository.getBodyWeightById(bodyWeight.id) //since the BodyWeight was deleted we now expect an Exception to be thrown  since it shouldn't be in the Database anymore
    }

    @Test(expected = IllegalArgumentException::class)
    fun testIncorrectDelete() {
        val bodyWeight = BodyWeight(700.0, Calendar.getInstance().time)
        bodyWeightRepository.deleteBodyWeight(bodyWeight)
    }



}