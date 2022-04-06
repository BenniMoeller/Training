package com.example.training.database.dataClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.training.database.DataConverter
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*

/**
 * dataclass that represents a bodyweight
 * @property weight Double the weight
 * @property date Date the date the bodyweight was recorded on. also the primary key
 */
@Entity(tableName = "bodyweight_table")
data class BodyWeight(@ColumnInfo(name = "weight") val weight: Double,
                      @PrimaryKey(autoGenerate = false) var date: Date = Calendar.getInstance().time) {

    @Ignore
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy") //the formatter to display the date of the bodyWeight


    init {
        date = DataConverter.serializeDate(date)
    }


    /**
     * formats the date of the BodyWeight in a string
     * @return String the string containing the day, month and year
     */
    fun asString(): String = "$weight ${dateFormat.format(date)}"


}