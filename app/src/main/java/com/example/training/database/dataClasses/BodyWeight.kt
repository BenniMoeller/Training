package com.example.training.database.dataClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

/**
 * dataclass that represents a bodyweight
 * @property id Long the id in the databases
 * @property weight Double the weight
 * @property date Date the date the bodyweight was recorded on
 */
@Entity(tableName = "bodyweight_table")
data class BodyWeight(@ColumnInfo(name = "weight") val weight: Double,
                      @ColumnInfo(name = "date") var date: Date,
                      @PrimaryKey(autoGenerate = true) var id: Long = 0) {
    @Ignore
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy") //the formatter to transform the BodyWeight to a simple String


    /**
     * formats the date of the BodyWeight in a string
     * @return String the string containing the day, month and year
     */
    fun asString(): String = "$weight ${dateFormat.format(date)}"

    //todo maybe change the primary key to the date so
}