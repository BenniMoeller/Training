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


    companion object {
        private val dateFormat = SimpleDateFormat("dd/MM/yyyy") //the formatter to serialize a date

        /**
         * serializes a date so it only consists of year, month and day
         * @param date Date the date to serialize
         * @return the serialized Date
         */
        fun serializeDate(date: Date): Date {
            val dateString = dateFormat.format(date)
            return dateFormat.parse(dateString)
        }
    }


    @TypeConverter
    fun dateToValue(data: Date?) = data?.time


    @TypeConverter
    fun valueToDate(data: Long?) = data?.let { Date(it) }


    @TypeConverter
    fun exerciseTypeToValue(exerciseType: ExerciseType): String = exerciseType.name

    @TypeConverter
    fun valueToExerciseType(data: String) = enumValueOf<ExerciseType>(data)

    @TypeConverter
    fun rangeToValue(data: Range) = data.asFormattedString()

    @TypeConverter
    fun valueToRange(data: String) = Range.fromFormattedString(data)


}

   