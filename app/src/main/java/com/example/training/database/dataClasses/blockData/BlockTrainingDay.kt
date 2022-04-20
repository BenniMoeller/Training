package com.example.training.database.dataClasses.blockData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.lang.IllegalArgumentException

/**
 * data class that represents a template for one normal TrainingDay
 * @property blockId Long the of the Block this BlockTrainingDay belongs to
 * @property dayOfWeek Int the weekday of this trainingDay from 0-6
 * @property id Long the id of this entity in the database
 * @constructor
 */
@Entity(tableName = "block_trainingday_table",
        foreignKeys = [ForeignKey(entity = Block::class,
                                  parentColumns = arrayOf("id"),
                                  childColumns = arrayOf("block_id"),
                                  onDelete = ForeignKey.CASCADE)])
data class BlockTrainingDay(@ColumnInfo(name = "block_id") var blockId: Long,
                            @ColumnInfo(name = "day_of_week") var dayOfWeek: Int,
                            @PrimaryKey(autoGenerate = true) var id: Long = 0) {

    init {
        if (dayOfWeek < 0 || dayOfWeek > 6) throw IllegalArgumentException("The Weekday has to be between 0 and 6")
    }


}