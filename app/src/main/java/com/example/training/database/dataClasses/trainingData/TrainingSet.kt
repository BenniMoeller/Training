package com.example.training.database.dataClasses.trainingData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.training.database.dataClasses.blockData.BlockSet
import java.lang.IllegalArgumentException

/**
 * entity that describes a trainingSet
 * @property weight Double the weight of the set
 * @property reps Int the reps of the set
 * @property rir Int the reps in reserve of this et
 * @property id Long the id in the database
 * @property trainingExerciseId Long the id of the exercise this set is a part of
 * @property blockSetId Long the id of the BlockSet that is the template for this set
 * @constructor
 */
@Entity(tableName = "trainingset_table",
        foreignKeys = [ForeignKey(entity = TrainingExercise::class,
                                  parentColumns = arrayOf("id"),
                                  childColumns = arrayOf("training_exercise"),
                                  onDelete = ForeignKey.CASCADE), ForeignKey(entity = BlockSet::class,
                                                                             parentColumns = arrayOf("id"),
                                                                             childColumns = arrayOf("block_set"),
                                                                             onDelete = ForeignKey.CASCADE)])
data class TrainingSet(@ColumnInfo(name = "weight") val weight: Double,
                       @ColumnInfo(name = "reps") val reps: Int,
                       @ColumnInfo(name = "rir") val rir: Int,
                       @PrimaryKey(autoGenerate = true) var id: Long = 0,
                       @ColumnInfo(name = "training_exercise") var trainingExerciseId: Long = 0,
                       @ColumnInfo(name = "block_set") var blockSetId: Long = 0) {
    init {
      //todo this check makes sense but it fucks with the databinding and creating an empty trainingSet. change this later  if ((reps < 1) || (rir < 0) || (weight < 0.0)) throw IllegalArgumentException("reps, rir and weight mustn't be smaller than zero")
    }

    /**
     * checks whether another trainingSet matches this one in terms of weight, reps and rir
     */
    fun equalsTrainingSet(trainingSet: TrainingSet): Boolean {
        return (weight == trainingSet.weight && reps == trainingSet.reps && rir == trainingSet.rir )
    }


}