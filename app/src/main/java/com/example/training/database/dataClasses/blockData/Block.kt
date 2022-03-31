package com.example.training.database.dataClasses.blockData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * class that represents a whole training Block
 * @property id Long the id in the database
 * @property name String the name of the block
 * @property isDevelopmentBlock Boolean wether the Block is a developmentBlock (true) or a Pivot Block (false)
 * @property start Date the startDate of the Block
 * @property end Date the end date of the block
 */
@Entity(tableName = "block_table")
data class Block(@ColumnInfo(name = "blockName") val name: String,
                 @ColumnInfo(name = "is_development_Block") val isDevelopmentBlock: Boolean,
                 @ColumnInfo(name = "block_start") val start: Date = Calendar.getInstance().time,
                 @PrimaryKey(autoGenerate = true) val id: Long = 0,
                 @ColumnInfo(name = "block_end") val end: Date? = null) {

}