package com.example.training.repositories

import android.database.sqlite.SQLiteConstraintException
import com.example.training.database.DatabaseDao
import com.example.training.database.dataClasses.blockData.Block
import com.example.training.database.dataClasses.blockData.BlockExercise
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.blockData.BlockTrainingDay
import java.lang.IllegalArgumentException


/**
 * class that acts as access to the Database for all the Block classes
 * all functions should only be called in background threads
 */
internal class BlockRepository(private val databaseDao: DatabaseDao) {

    /**
     * saves a block to the database.
     * @param block Block
     */
    fun saveBlock(block: Block) {
        block.id = databaseDao.saveBlock(block)
    }


    /**
     * saves a BlockTrainingDay to the Database
     * @param blockTrainingDay BlockTrainingDay
     * @param blockId Long the id of the block this BlockTrainingDay belongs to
     * @param dayOfWeek Int the day that this Block is executed on
     */
    fun saveBlockTrainingDay(blockTrainingDay: BlockTrainingDay) {
        if (databaseDao.getBlockTrainingDayByBlockAndWeekDay(blockTrainingDay.blockId, blockTrainingDay.dayOfWeek) != null) {
            throw IllegalArgumentException("there Already exists a BlockTrainingDay for this Block and dayOfWeek")
        }


        try {
            blockTrainingDay.id = databaseDao.saveBlockTrainingDay(blockTrainingDay)
        } catch (error: SQLiteConstraintException) {
            throw IllegalArgumentException("there exists no Block with the Id ${blockTrainingDay.blockId}")
        }

    }


    /**
     * saves a blockExercise to the Database
     * @param blockExercise BlockExercise
     * @param blockTrainingDayId Long the if of the BlockTrainingDay this entity belongs to
     * @param exerciseCounter Int the index of this BlockExercise in the BlockTrainingDay
     * @param exerciseName String the name of the Exercise of this BlockExercise
     */
    fun saveBlockExercise(blockExercise: BlockExercise) {
        if (databaseDao.getBlockExerciseFromBlockTrainingDayAndExerciseCounter(blockExercise.blockTrainingDayId,
                                                                               blockExercise.exerciseCounter) != null) {
            throw IllegalArgumentException("There already exists a BlockExercise for this BlockTrainingDay with this counter")
        }
        if (databaseDao.getExerciseByName(blockExercise.exerciseName) == null) {
            throw IllegalArgumentException("The exercise ${blockExercise.exerciseName} doesn't exist in the database")
        }

        try {
            blockExercise.id = databaseDao.saveBlockExercise(blockExercise)
        } catch (error: SQLiteConstraintException) {
            throw IllegalArgumentException("there exists no combination of BlockTrainingDay and Exercise for this BLockExercise")
        }
    }

    /**
     * saves a BlockSet to the dataBase
     * @param blockSet BlockSet the BlockSet to be saved
     */
    fun saveBlockSet(blockSet: BlockSet) {
        if (databaseDao.getBlockSetByBlockExerciseAndSetIndex(blockSet.blockExerciseId, blockSet.setIndex) != null) {
            throw IllegalArgumentException("There already exists a BlockSet for this BlockExercise and setIndex")
        }

        try {
            blockSet.id = databaseDao.saveBlockSet(blockSet)
        } catch (error: SQLiteConstraintException) {
            throw IllegalArgumentException("There exists no BlockExercise with the id ${blockSet.blockExerciseId}")
        }
    }

}
