package com.example.training.database.dataClasses.trainingData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.training.database.dataClasses.blockData.BlockExercise

/**
 *
 * @property exerciseOrder Int the order of the exercise in a training day
 * @property id Long the id in the database
 * @property blockTrainingExerciseId Long the template which this is a part of
 * @property trainingDayId Long the id of the trainingDay this entity takes place in
 * @constructor
 */
@Entity(tableName = "trainingexercise_table",
        foreignKeys = [ForeignKey(entity = BlockExercise::class,
                                  parentColumns = arrayOf("id"),
                                  childColumns = arrayOf("blocktraining_exercise"),
                                  onDelete = ForeignKey.CASCADE), ForeignKey(entity = TrainingDay::class,
                                                                             parentColumns = arrayOf("id"),
                                                                             childColumns = arrayOf("training_day"),
                                                                             onDelete = ForeignKey.CASCADE)])
data class TrainingExercise(@ColumnInfo(name = "exercise_order") val exerciseOrder: Int,
                            @PrimaryKey(autoGenerate = true) val id: Long = 0,
                            @ColumnInfo(name = "blocktraining_exercise") val blockTrainingExerciseId: Long = 0,
                            @ColumnInfo(name = "training_day") val trainingDayId: Long = 0) {


}