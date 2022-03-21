package com.example.training.database.dataClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * dataclass that represents a bodyweight
 * @property id Long the id in the databases
 * @property weight Double the weight
 * @property date Date the date the bodyweight was recorded on
 */
@Entity(tableName = "bodyweight_table")
data class BodyWeight(@PrimaryKey(autoGenerate = true) val id: Long,
                      @ColumnInfo(name = "weight") val weight: Double,
                      @ColumnInfo(name = "date") val date: Date
) {

}