package com.example.training.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.training.database.dataClasses.BodyWeight
import com.example.training.database.dataClasses.Exercise
import java.util.*

/**
 * the dao for the database
 */
@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //replaces BodyWeight so today's bodyweight can still be updated
    fun saveBodyWeight(bodyWeight: BodyWeight): Long

    @Query("SELECT * FROM bodyweight_table")
    fun getAllBodyWeights(): LiveData<List<BodyWeight>>

    @Delete
    fun deleteBodyWeight(bodyWeight: BodyWeight): Int

    @Query("SELECT * FROM bodyweight_table WHERE date = :bodyWeightDate")
    fun getBodyWeightById(bodyWeightDate: Date): BodyWeight?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun saveExercise(exercise: Exercise)

    @Delete
    fun deleteExercise(exercise: Exercise): Int

    @Query("SELECT * FROM exercise_table")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Update
    fun updateExercise(exercise: Exercise): Int

    @Query("SELECT * FROM exercise_table WHERE name = :exerciseName")
    fun getExerciseByName(exerciseName: String): Exercise?

}