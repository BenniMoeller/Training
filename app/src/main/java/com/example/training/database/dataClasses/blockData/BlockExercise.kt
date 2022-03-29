package com.example.training.database.dataClasses.blockData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.training.database.dataClasses.Exercise
import java.lang.IllegalArgumentException

/**
 * class representing a blockExercise template for the database
 * @property id Long the id in the database
 * @property blockTrainingDayId Long the id of the BlockTrainingDay this BlockExercise belongs to
 * @property exerciseId Long the id the exercise in this blockExercise
 * @property exerciseCounter Int the counter of the exercise
 * @constructor
 */
@Entity(tableName = "block_exercise_table",
        foreignKeys = [ForeignKey(entity = BlockTrainingDay::class,
                                  parentColumns = arrayOf("id"),
                                  childColumns = arrayOf("block_trainingday_id"),
                                  onDelete = ForeignKey.CASCADE), ForeignKey(entity = Exercise::class,
                                                                             parentColumns = arrayOf("name"),
                                                                             childColumns = arrayOf("exercise_name"),
                                                                             onDelete = ForeignKey.SET_NULL)]

       )
data class BlockExercise(@ColumnInfo(name = "exercise_counter") val exerciseCounter: Int,
                         @PrimaryKey(autoGenerate = true) val id: Long = 0,
                         @ColumnInfo(name = "block_trainingday_id") val blockTrainingDayId: Long = 0,
                         @ColumnInfo(name = "exercise_name") val exerciseName: String = "") {

    init {
        if (exerciseCounter < 0) throw IllegalArgumentException("The exerciseCounter can't be smaller than 0")
    }


}