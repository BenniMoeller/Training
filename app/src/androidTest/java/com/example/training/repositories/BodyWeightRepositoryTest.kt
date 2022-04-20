package com.example.training.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
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
    fun testSaveAndGetByDateBodyWeight() {
        val bodyWeight1 = BodyWeight(90.0)
        val bodyWeight2 = BodyWeight(100.0, Calendar.getInstance().apply { this.set(Calendar.YEAR, 2020) }.time)
        bodyWeightRepository.saveBodyWeight(bodyWeight1)
        bodyWeightRepository.saveBodyWeight(bodyWeight2)

        val storedBodyWeight = bodyWeightRepository.getBodyWeightByDate(bodyWeight1.date)
        assertTrue(storedBodyWeight.weight == bodyWeight1.weight)
        assertTrue(storedBodyWeight.date == bodyWeight1.date)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testIncorrectGetByDate() {
        bodyWeightRepository.getBodyWeightByDate(Calendar.getInstance().apply { this.set(Calendar.YEAR, 4059) }.time)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testDeleteBodyWeight() {
        val bodyWeight = BodyWeight(50.0, Calendar.getInstance().time)
        bodyWeightRepository.saveBodyWeight(bodyWeight)
        bodyWeightRepository.deleteBodyWeight(bodyWeight)
        bodyWeightRepository.getBodyWeightByDate(bodyWeight.date) //since the BodyWeight was deleted we now expect an Exception to be thrown  since it shouldn't be in the Database anymore
    }

    @Test(expected = IllegalArgumentException::class)
    fun testIncorrectDelete() {
        val bodyWeight = BodyWeight(700.0, Calendar.getInstance().time)
        bodyWeightRepository.deleteBodyWeight(bodyWeight)
    }


}