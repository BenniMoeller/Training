package com.example.training.database.dataClasses.trainingData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.training.database.dataClasses.blockData.BlockTrainingDay
import java.lang.IllegalArgumentException
import java.util.*

/**
 * entity that describes a training day
 * @property weekIndex Int the how manyth week this is in the block
 * @property id Long the id of this entity in the database
 * @property  Long the id of the BlockTrainingDay this
 * @constructor
 */
@Entity(tableName = "training_day_table",
        foreignKeys = [ForeignKey(entity = BlockTrainingDay::class,
                                  parentColumns = arrayOf("id"),
                                  childColumns = arrayOf("blocktrainingday_id"),
                                  onDelete = ForeignKey.CASCADE)])
data class TrainingDay(@ColumnInfo(name = "week_index") val weekIndex: Int,
                       @PrimaryKey(autoGenerate = true) var id: Long = 0,
                       @ColumnInfo(name = "blocktrainingday_id") val blockTrainingDayId: Long = 0) {

    init {
        if (weekIndex < 0) throw IllegalArgumentException("There can't be nothing before the first Week in a Block")
    }


}