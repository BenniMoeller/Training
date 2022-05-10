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
    fun saveBlock(block: Block) { //todo make sure that all other blocks are finished when this one is saved
        block.id = databaseDao.saveBlock(block)
    }

    /**
     * returns the currently active Block
     * @return Block
     */
    fun getCurrentBlock() = databaseDao.getLatestActiveBlock()


    /**
     * saves a BlockTrainingDay to the Database
     * @param blockTrainingDay BlockTrainingDay //todo make sure that for all block data classes that there are no gaps in the indices
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
     * returns the BlockTrainingDay from the database
     * @param blockId Long the of the Block this BlockTrainingDay takes place in
     * @param dayOfWeek Int the day of the week that this BlockTrainingDay is planned on
     */
    fun getBlockTrainingDay(blockId: Long, dayOfWeek: Int) = databaseDao.getBlockTrainingDayByBlockAndWeekDay(blockId, dayOfWeek)
    //todo maybe throw exception if the Block doesn't exist

    /**
     * saves a blockExercise to the Database
     * @param blockExercise BlockExercise
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
     * returns the BlockExercise from the database
     * @param blockTrainingDayId Long the id of the BlockTrainingDay this BlockExercise takes place in
     * @param exerciseCounter Int the how manyth BlockExercise this is in a BlockTrainingDay
     * @return BlockExercise?
     */
    fun getBlockExercise(blockTrainingDayId: Long, exerciseCounter: Int) = //todo maybe throw exception if the BlockTrainingDay doesn't exist
        databaseDao.getBlockExerciseFromBlockTrainingDayAndExerciseCounter(blockTrainingDayId, exerciseCounter)

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

    /**
     * returns all the BlockSets from a BlockExercise
     * @param blockExerciseId Long the id of the BLockExercise in the Database
     * @return List<BlockSet> the BlockSets in a list
     */
    fun getTrainingSetsFromBlockExercise(blockExerciseId: Long) = databaseDao.getBlockSetsFromBlockExercise(blockExerciseId)
    //todo maybe throw exception if the BlockExercise doesn't exist

}
