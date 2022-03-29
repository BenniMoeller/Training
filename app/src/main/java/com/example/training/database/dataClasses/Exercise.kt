package com.example.training.database.dataClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * class that represents an exercise in the database
 * @property name String the name of the Exericse. also the primary key
 * @property exerciseType ExerciseType the type of the exercise eg. lower body pull
 * @property isMainLift Boolean whether the exercise is treated as mainLift and thus important
 * @constructor
 */
@Entity(tableName = "exercise_table")
data class Exercise(@PrimaryKey(autoGenerate = false) val name: String,
                    @ColumnInfo(name = "exercise_type") val exerciseType: ExerciseType,
                    @ColumnInfo(name = "is_mainlift") var isMainLift: Boolean = false) {


    /**
     * returns the exercise with a formatted string containing the name and type
     * @return String the formatted string
     */
    fun asFormattedString() = "$name  ${exerciseType.name}"


}

/**
 * class that defines an exerciseType
 */
enum class ExerciseType {
    LOWER_BODY_PUSH, LOWER_BODY_PULL, UPPER_BODY_HOR_PUSH, UPPER_BODY_VERT_PUSH, UPPER_BODY_HOR_PULL, UPPER_BODY_VER_PULL, BICEPS, TRICEPS,
    SIDE_DELTS, REAR_DELTS, CALVES, CORE
}