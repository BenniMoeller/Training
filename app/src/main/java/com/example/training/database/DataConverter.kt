package com.example.training.database

import androidx.room.TypeConverter
import com.example.training.database.dataClasses.ExerciseType
import com.example.training.database.dataClasses.blockData.Range
import java.text.SimpleDateFormat
import java.util.*


/**
 * converts database classes so that they can be stored as string
 */
internal class DataConverter {
    private val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy") //the dateFormat used to serialize the dates for the database

    /**
     * we only want to save the year, month and day of the date
     */
    @TypeConverter
    fun dateToValue(data: Date): Long {
        val dateString = simpleDateFormat.format(data) //the format is used to only save the year, month, and day of the date
        return simpleDateFormat.parse(dateString).time
    }


    @TypeConverter
    fun valueToDate(data: Long) = Date(data)


    /**
     * serializes a date so it only consists of year, month and day
     * @param date Date the date to be converted
     * @return Date the converted date
     */
    fun serializeDate(date: Date) = valueToDate(dateToValue(date))

    @TypeConverter
    fun exerciseTypeToValue(exerciseType: ExerciseType): String = exerciseType.name

    @TypeConverter
    fun valueToExerciseType(data: String) = enumValueOf<ExerciseType>(data)

    @TypeConverter
    fun rangeToValue(data: Range) = data.asFormattedString()

    @TypeConverter
    fun valueToRange(data: String): Range {
        val numbers = data.split("-")
        return Range(numbers[0].toInt(), numbers[1].toInt())
    }




}

   