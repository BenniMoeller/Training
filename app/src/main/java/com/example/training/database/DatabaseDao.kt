package com.example.training.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.training.database.dataClasses.BodyWeight
import com.example.training.database.dataClasses.Exercise
import com.example.training.database.dataClasses.blockData.Block
import com.example.training.database.dataClasses.blockData.BlockExercise
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.blockData.BlockTrainingDay
import com.example.training.database.dataClasses.trainingData.TrainingDay
import com.example.training.database.dataClasses.trainingData.TrainingExercise
import com.example.training.database.dataClasses.trainingData.TrainingSet
import java.util.*

/**
 * the dao for the database
 */
@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //replaces BodyWeight so today's bodyweight can still be updated
    fun saveBodyWeight(bodyWeight: BodyWeight): Long

    @Query("SELECT * FROM bodyweight_table ORDER BY date DESC")
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

    @Query("SELECT * FROM block_table WHERE block_end IS NULL ORDER BY block_start DESC LIMIT 1")
    fun getLatestActiveBlock(): Block?

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

    @Query("SELECT * FROM" + " blockset_table WHERE blockexercise_id = :blockExerciseId ORDER BY set_index ASC")
    fun getBlockSetsFromBlockExercise(blockExerciseId: Long): List<BlockSet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTrainingDay(trainingDay: TrainingDay): Long

    @Query("SELECT * FROM training_day_table WHERE blocktrainingday_id = :blockTrainingDayId AND week_index = :weekIndex")
    fun getTrainingDay(blockTrainingDayId: Long, weekIndex: Int): TrainingDay?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTrainingExercise(trainingExercise: TrainingExercise): Long

    @Query("DELETE FROM trainingset_table WHERE training_exercise = :trainingExerciseId AND block_set = :blockSetId")
    fun deleteTrainingSetFromTrainingExerciseAndBlockSet(trainingExerciseId: Long, blockSetId: Long)

    @Query("SELECT * FROM trainingexercise_table WHERE training_day_id = :trainingDayId AND blocktraining_exercise_id = :blockExerciseId")
    fun getTrainingExerciseByTrainingDayAndBlockExercise(trainingDayId: Long, blockExerciseId: Long): TrainingExercise?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTrainingSet(trainingSet: TrainingSet): Long

    @Query("SELECT * FROM trainingset_table WHERE training_exercise = (SELECT training_exercise.id FROM trainingexercise_table training_exercise INNER JOIN block_exercise_table block_exercise ON training_exercise.blocktraining_exercise_id = block_exercise.id WHERE block_exercise.exercise_index = :exerciseIndex AND training_exercise.training_day_id = (SELECT day_table.id FROM training_day_table day_table WHERE day_table.blocktrainingday_id = :blockTrainingDayId AND day_table.week_index = :weekIndex))")
    fun getTrainingSetsFromBlockTrainingDayAndWeekIndexAndExerciseCounter(blockTrainingDayId: Long,
                                                                          weekIndex: Int,
                                                                          exerciseIndex: Int): List<TrainingSet>

}