package com.example.training.database.dataClasses.trainingData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.training.database.dataClasses.blockData.BlockExercise

/**
 *
 * @property id Long the id of this entity in the database
 * @property trainingDayId Long the id of the trainingDay this trainingExercise takes place in
 * @property trainingDayId Long the id of the trainingDay this entity takes place in
 * @constructor
 */
@Entity(tableName = "trainingexercise_table",
        foreignKeys = [ForeignKey(entity = BlockExercise::class,
                                  parentColumns = arrayOf("id"),
                                  childColumns = arrayOf("blocktraining_exercise_id"),
                                  onDelete = ForeignKey.CASCADE), ForeignKey(entity = TrainingDay::class,
                                                                             parentColumns = arrayOf("id"),
                                                                             childColumns = arrayOf("training_day_id"),
                                                                             onDelete = ForeignKey.CASCADE)])
data class TrainingExercise(@PrimaryKey(autoGenerate = true) val id: Long = 0,
                            @ColumnInfo(name = "training_day_id") val trainingDayId: Long = 0,
                            @ColumnInfo(name = "blocktraining_exercise_id") val blockTrainingDayId: Long = 0) {


}