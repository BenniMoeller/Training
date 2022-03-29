package com.example.training.database.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.training.database.TrainingDatabase
import com.example.training.database.dataClasses.Exercise
import com.example.training.database.dataClasses.ExerciseType
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.IllegalArgumentException

class ExerciseRepositoryTest {

    private lateinit var exerciseRepository: ExerciseRepository
    private lateinit var dataBase: TrainingDatabase


    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dataBase = Room.inMemoryDatabaseBuilder(context, TrainingDatabase::class.java).build()
        exerciseRepository = ExerciseRepository(dataBase.databaseDao)
    }

    @After
    fun tearDown() {
        dataBase.close()
    }

    @Test
    fun testSaveAndGetByIdExercise() {
        val exercise1 = Exercise("Squat", ExerciseType.LOWER_BODY_PUSH)
        val exercise2 = Exercise("deadlift", ExerciseType.LOWER_BODY_PULL)
        exerciseRepository.saveExercise(exercise1)
        exerciseRepository.saveExercise(exercise2)

        val storedExercise1 = exerciseRepository.getExerciseById(exercise1.name)
        assertEquals(exercise1, storedExercise1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testOverwriteExercise() {
        val exercise = Exercise("Curls", ExerciseType.BICEPS)
        exerciseRepository.saveExercise(exercise)
        val exercise2 = Exercise("Curls", ExerciseType.CALVES)
        exerciseRepository.saveExercise(exercise2)

    }

    @Test(expected = IllegalArgumentException::class)
    fun testIncorrectGetById() {
        exerciseRepository.getExerciseById("wrongName")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testDelete() {
        val exercise = Exercise("bench", ExerciseType.UPPER_BODY_HOR_PUSH)
        exerciseRepository.saveExercise(exercise)
        exerciseRepository.deleteExercise(exercise)
        exerciseRepository.getExerciseById(exercise.name) //since the exercise is now deleted we expect an IllegalArgumentException when we search it
    }

    @Test(expected = IllegalArgumentException::class)
    fun testIncorrectDelete() {
        val exercise = Exercise("OHP", ExerciseType.UPPER_BODY_VERT_PUSH)
        exerciseRepository.deleteExercise(exercise)
    }

    @Test
    fun testChangeMainLiftStatus() {
        val exercise = Exercise("Pendlay Rows", ExerciseType.UPPER_BODY_HOR_PULL)
        exerciseRepository.saveExercise(exercise)
        exerciseRepository.changeMainLiftStatusOfExercise(exercise)
        assertTrue(exercise.isMainLift)

        val storedExercise = exerciseRepository.getExerciseById(exercise.name)
        assertEquals(exercise.isMainLift, storedExercise.isMainLift)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testIncorrectChangeMainLiftStatus() {
        val exercise = Exercise("Pull Ups", ExerciseType.UPPER_BODY_VER_PULL)
        exerciseRepository.changeMainLiftStatusOfExercise(exercise)
    }

}