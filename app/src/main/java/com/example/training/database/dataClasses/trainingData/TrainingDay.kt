package com.example.training.database.dataClasses.trainingData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.training.database.dataClasses.blockData.BlockTrainingDay
import java.util.*

/**
 * entity that describes a training day
 * @property date Date the date this training took place in
 * @property id Long the id of this entity in the database
 * @property blockTrainingId Long the id of the BlockTrainingDay this
 * @constructor
 */
@Entity(tableName = "training_day_table",
        foreignKeys = [ForeignKey(entity = BlockTrainingDay::class,
                                  parentColumns = arrayOf("id"),
                                  childColumns = arrayOf("block_training_day"),
                                  onDelete = ForeignKey.CASCADE)])
data class TrainingDay(@ColumnInfo(name = "date") val date: Date,
                       @PrimaryKey(autoGenerate = true) val id: Long = 0,
                       @ColumnInfo(name = "block_training_day") val blockTrainingId: Long = 0) {


}