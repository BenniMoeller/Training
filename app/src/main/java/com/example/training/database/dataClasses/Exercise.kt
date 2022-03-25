package com.example.training.database.dataClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * class that represents an exercise in the database
 * @property id Long the id in the dataBase
 * @property exerciseName String the name of the Exericse
 * @property exerciseType ExerciseType the type of the exercise eg. lower body pull
 * @property isMainLift Boolean whether the exercise is treated as mainLift and thus important
 * @constructor
 */
@Entity(tableName = "exercise_table")
data class Exercise private constructor(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "exercise_name") val exerciseName: String,
    @ColumnInfo(name = "exercise_type") val exerciseType: ExerciseType,
    @ColumnInfo(name = "is_mainlift") var isMainLift: Boolean
) {

    /**
     * the main constructor without an id or mainLift
     * @param exerciseName String the name of the Exercise
     * @param exerciseType ExerciseType the type of the exercise
     */
    constructor(exerciseName: String, exerciseType: ExerciseType) : this(0, exerciseName, exerciseType, false)

    /**
     * returns the exercise with a formatted string containing the name and type
     * @return String the formatted string
     */
    fun asFormattedString() = "$exerciseName  ${exerciseType.name}"


}

/**
 * class that defines an exerciseType
 */
enum class ExerciseType {
    LOWER_BODY_PUSH, LOWER_BODY_PULL, UPPER_BODY_HOR_PUSH, UPPER_BODY_VERT_PUSH, UPPER_BODY_HOR_PULL, UPPER_BODY_VER_PULL, BICEPS, TRICEPS,
    SIDE_DELTS, REAR_DELTS, CALVES, CORE
}