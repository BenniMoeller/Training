package com.example.training.database.dataClasses.blockData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.training.database.dataClasses.Exercise
import java.lang.IllegalArgumentException

/**
 * class representing a blockExercise template for the database
 * @property exerciseCounter Int the index of the exercise in a
 * @property id Long the id of this entity in the database
 * @property blockTrainingDayId Long the weekDay of the BlockTrainingDay this BlockExercise belongs to
 * @property exerciseName Long the name the exercise in this blockExercise
 * @constructor
 */
@Entity(tableName = "block_exercise_table",
        foreignKeys = [ForeignKey(entity = BlockTrainingDay::class,
                                  parentColumns = arrayOf("id"),
                                  childColumns = arrayOf("block_trainingday_id"),
                                  onDelete = ForeignKey.CASCADE), ForeignKey(entity = Exercise::class,
                                                                             parentColumns = arrayOf("name"),
                                                                             childColumns = arrayOf("exercise_name"),
                                                                             onDelete = ForeignKey.SET_NULL)]) //todo change this to ondelete set deafult
data class BlockExercise(@ColumnInfo(name = "exercise_index") val exerciseCounter: Int,
                         @PrimaryKey(autoGenerate = true) val id: Long = 0,
                         @ColumnInfo(name = "block_trainingday_id") val blockTrainingDayId: Long = 0,
                         @ColumnInfo(name = "exercise_name") val exerciseName: String = "") {

    init {
        if (exerciseCounter < 0) throw IllegalArgumentException("The exerciseCounter can't be smaller than 0")
    }


}