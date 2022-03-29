package com.example.training.database.repositories

import android.database.sqlite.SQLiteConstraintException
import com.example.training.database.DatabaseDao
import com.example.training.database.dataClasses.Exercise
import java.lang.IllegalArgumentException


/**
 * class that acts as access to the Database for the Exercise
 * all functions should only be called in background threads
 */
internal class ExerciseRepository(private val databaseDao: DatabaseDao) {

    /**
     * saves an exercise to the database and updates its id
     * @param exercise Exercise the exercise to be saved
     */
    fun saveExercise(exercise: Exercise) {
        try {
            databaseDao.saveExercise(exercise)
        } catch (exception: SQLiteConstraintException) {
            throw IllegalArgumentException("An exercise with that Name was already inserted in the dataBase")
        }
    }

    /**
     * deletes an exercise from the database
     * @param exercise Exercise the exercise to be deletes
     */
    fun deleteExercise(exercise: Exercise) {
        val wasDeleted = databaseDao.deleteExercise(exercise)
        if (wasDeleted == 0) throw IllegalArgumentException("Exercise was not found in the dataBase")
    }

    /**
     * returns all exercises from the database
     * @return LiveData<List<Exercise>> a livedata containing a list containing all the exercises
     */
    fun getAllExercises() = databaseDao.getAllExercises()

    /**
     * changes the mainLiftstatus of an exercise and updates the value in the database
     * @param exercise Exercise
     */
    fun changeMainLiftStatusOfExercise(exercise: Exercise) {
        exercise.isMainLift = !exercise.isMainLift
        val updateSuccessful = databaseDao.updateExercise(exercise)
        if (updateSuccessful == 0) throw IllegalArgumentException("the exercise could not be found in the database")
    }

    /**
     * retrieves an exercise from the database
     * @param exerciseName Long the name of the exercise
     * @return Exercise
     */
    fun getExerciseById(exerciseName: String): Exercise {
        return databaseDao.getExerciseByName(exerciseName) ?: throw IllegalArgumentException("this exercise was not found in the database")
    }

}
