package com.example.training.database.dataClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
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
    constructor(weight: Double, date: Date) : this(0, weight, date) //this constructor is used to create the entry without an id

    @Ignore
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy") //the formatter to transform the BodyWeight to a simple String


    /**
     * formats the date of the BodyWeight in a string
     * @return String the string containing the day, month and year
     */
    fun asString(): String = "$weight ${dateFormat.format(date)}"

    //todo maybe change the primary key to the date so
}