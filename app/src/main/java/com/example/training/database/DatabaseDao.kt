package com.example.training.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.training.database.dataClasses.BodyWeight
import com.example.training.database.dataClasses.Exercise
import com.example.training.database.dataClasses.blockData.Block
import com.example.training.database.dataClasses.blockData.BlockExercise
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.blockData.BlockTrainingDay
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

    @Query("SELECT name FROM exercise_table SORT")
    fun getAllExerciseNames(): LiveData<List<String>>

    @Update
    fun updateExercise(exercise: Exercise): Int

    @Query("SELECT * FROM exercise_table WHERE name = :exerciseName")
    fun getExerciseByName(exerciseName: String): Exercise?

    @Insert
    fun saveBlock(block: Block): Long

    @Insert
    fun saveBlockTrainingDay(block: BlockTrainingDay): Long

    @Query("SELECT * FROM block_trainingday_table WHERE day_of_week = :dayOfWeek AND block_id = :blockId")
    fun getBlockTrainingDayByBlockAndWeekDay(blockId: Long, dayOfWeek: Int): BlockTrainingDay?

    @Insert()
    fun saveBlockExercise(blockExercise: BlockExercise): Long

    @Query("SELECT * FROM block_exercise_table WHERE block_trainingday_id = :blockTrainingDayId AND exercise_index = :exerciseCounter ")
    fun getBlockExerciseFromBlockTrainingDayAndExerciseCounter(blockTrainingDayId: Long, exerciseCounter: Int): BlockExercise?

    @Insert
    fun saveBlockSet(blockSet: BlockSet): Long

    @Query("SELECT * FROM blockset_table WHERE blockexercise_id = :blockExerciseId AND set_index = :setIndex")
    fun getBlockSetByBlockExerciseAndSetIndex(blockExerciseId: Long, setIndex: Int): BlockSet?


}