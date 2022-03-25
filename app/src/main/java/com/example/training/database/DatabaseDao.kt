package com.example.training.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.training.database.dataClasses.BodyWeight
import com.example.training.database.dataClasses.Exercise

/**
 * the dao for the database
 */
@Dao
interface DatabaseDao {

    @Insert
    fun saveBodyWeight(bodyWeight: BodyWeight): Long

    @Query("SELECT * FROM bodyweight_table")
    fun getAllBodyWeights(): LiveData<List<BodyWeight>>

    @Delete
    fun deleteBodyWeight(bodyWeight: BodyWeight): Int

    @Insert
    fun saveExercise(exercise: Exercise): Long

    @Delete
    fun deleteExercise(exercise: Exercise): Int

    @Query("SELECT * FROM exercise_table")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Update
    fun updateExercise(exercise: Exercise): Int

}