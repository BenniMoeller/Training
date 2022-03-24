package com.example.training.database

import androidx.room.TypeConverter
import com.example.training.database.dataClasses.ExerciseType
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

    @TypeConverter
    fun exerciseTypeToValue(exerciseType: ExerciseType): String = exerciseType.name

    @TypeConverter
    fun valueToExerciseType(data: String) = enumValueOf<ExerciseType>(data)


}

   