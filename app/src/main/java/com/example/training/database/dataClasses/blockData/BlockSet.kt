package com.example.training.database.dataClasses.blockData

import androidx.room.*
import java.lang.IllegalArgumentException
import kotlin.math.max

/**
 * class that represents a template for a single trainingSet
 * @property id Long the id in the database
 * @property blockExerciseId Long the id of the BlockExercise this set takes place in
 * @property targetReps Range the reps of this set
 * @property targetRir Range the RepsInReserve of this set
 * @constructor
 */
@Entity(tableName = "blockset_table",
        foreignKeys = [ForeignKey(entity = BlockExercise::class,
                                  parentColumns = arrayOf("id"),
                                  childColumns = arrayOf("blockexercise_id"),
                                  onDelete = ForeignKey.CASCADE)])
data class BlockSet(@ColumnInfo(name = "target_reps") val targetReps: Range,
                                        @ColumnInfo(name = "target_rir") val targetRir: Range,
                                        @PrimaryKey(autoGenerate = true) val id: Long = 0,
                                        @ColumnInfo(name = "blockexercise_id") val blockExerciseId: Long = 0) {


}

/**
 * class that describes a range for integers
 * @property minRange Int the start of the range
 * @property maxRange Int the end of the range
 * @constructor
 */
data class Range(private val minRange: Int, private val maxRange: Int) {
    @Ignore
    val separator = "-" //the separator to display this range as a string

    init {
        if (minRange < 0 && maxRange < 0) throw IllegalArgumentException("reps mustn't be smaller than zero")
        if (minRange > maxRange) throw IllegalArgumentException("the minReps mustn't be smaller than the maxReps ")
    }

    /**
     * returns this Range as a formatted string
     * @return String the formatted string.
     */
    fun asFormattedString(): String {
        return if (minRange == maxRange) {
            minRange.toString()
        } else {
            "$minRange$separator$maxRange"
        }
    }


}