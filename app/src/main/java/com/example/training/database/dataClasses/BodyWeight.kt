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
data class BodyWeight private constructor(
    @PrimaryKey(autoGenerate = true) var id: Long, @ColumnInfo(name = "weight") val weight: Double, @ColumnInfo(name = "date") val date: Date
) {
    constructor(weight: Double, date: Date) : this(0, weight, date) //this constructor is used to create bodyWeights without an id

    /**
     * returns the bodyWeight as a String
     * @return String the formatted String
     */
    fun asString() = "$date   $weight" //todo date is displayed terribly. only show day, month and year

    //todo maybe change the primary key to the date so
}