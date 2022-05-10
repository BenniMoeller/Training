package com.example.training.repositories

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Query
import com.example.training.database.DatabaseDao
import com.example.training.database.dataClasses.trainingData.TrainingDay
import com.example.training.database.dataClasses.trainingData.TrainingExercise
import com.example.training.database.dataClasses.trainingData.TrainingSet
import java.lang.IllegalArgumentException
import java.util.*


/**
 * class that acts as access to the Database for all of the Training Classes
 * all functions should only be called in background threads
 */
internal class TrainingRepository(private val databaseDao: DatabaseDao) {

    /**
     * saves the TrainingDay to the Database
     * @param trainingDay TrainingDay
     * @throws IllegalArgumentException if the trainingDay.blockTrainingDayId doesn't exist in the database
     */
    fun saveTrainingDay(trainingDay: TrainingDay) {
        try {
            trainingDay.id = databaseDao.saveTrainingDay(trainingDay)
        } catch (error: SQLiteConstraintException) {
            throw IllegalArgumentException("The BlockTrainingDay with the id ${trainingDay.blockTrainingDayId} doesn't exist in the database")
        }
    }

    /**
     * returns the trainingDay from the database
     * @param blockTrainingDayId Long the of the blockTrainingDay this TrainingDay takes place in
     * @param weekIndex Int index of the week this TrainingDay takes place in
     * @return TrainingDay? the wanted TrainingDay or null if such a day doesn't exist in the database
     */
    fun getTrainingDay(blockTrainingDayId: Long, weekIndex: Int) = databaseDao.getTrainingDay(blockTrainingDayId, weekIndex)


    /**
     * saves the TrainingExercise to the database
     * @param trainingExercise TrainingExercise
     * @throws IllegalArgumentException if the trainingExercise.trainingDayId or the trainingExercise.blockTrainingDayId aren't in the database
     */
    fun saveTrainingExercise(trainingExercise: TrainingExercise) {
        try {
            trainingExercise.id = databaseDao.saveTrainingExercise(trainingExercise)
        } catch (error: SQLiteConstraintException) {
            throw IllegalArgumentException("Either the TrainingDay ${trainingExercise.trainingDayId} or the BlockExercise ${trainingExercise.blockTrainingExerciseId} don't exist")
        }
    }

    /**
     * retrieves a TrainingExercise from the database
     * @param trainingDayId Long the id of the TrainingDay this TrainingExercise takes place in
     * @param blockExerciseId Long the id of the BlockExercise this Trainingday takes place in
     * @return TrainingExercise?
     */
    fun getTrainingExercise(trainingDayId: Long, blockExerciseId: Long) =
        databaseDao.getTrainingExerciseByTrainingDayAndBlockExercise(trainingDayId, blockExerciseId)

    /**
     * saves the TrainingSet to the database
     * @param trainingSet TrainingSet
     * @throws IllegalArgumentException if the trainingSet.trainingExerciseId or the trainingSet.blockSetId don't exist in the database
     */
    fun saveTrainingSet(trainingSet: TrainingSet) {
        databaseDao.deleteTrainingSetFromTrainingExerciseAndBlockSet(trainingSet.trainingExerciseId, trainingSet.blockSetId)
        try {
            trainingSet.id = databaseDao.saveTrainingSet(trainingSet)
        } catch (error: SQLiteConstraintException) {
            throw IllegalArgumentException("Either the TrainingExercise ${trainingSet.trainingExerciseId} or the BlockSet ${trainingSet.blockSetId} don't exist")
        }
    }

    /**
     * returns a list of TrainingSets from the database
     * @param blockTrainingDayId Long the id of the BlockTrainingDay where the sets take place in
     * @param weekIndex the index of the week for the TrainingDay where the sets take place in
     * @param exerciseIndex Int the index of the TrainingExercise the the BlockSets are situated in
     */
    fun getTrainingSets(blockTrainingDayId: Long, weekIndex: Int, exerciseIndex: Int) =
        databaseDao.getTrainingSetsFromBlockTrainingDayAndWeekIndexAndExerciseCounter(blockTrainingDayId, weekIndex, exerciseIndex)

    /**
     * returns the amount of weeks that have passed since a blocks startdate
     * @param startDate Date the startDate of the block
     * @return Int the number of weeks that have passed
     */
    fun getTodayWeekIndex(startDate: Date): Int {
        val calendar = Calendar.getInstance()
        val todaysCalendar = Calendar.getInstance().apply { time = startDate }
        val weekIndex = todaysCalendar.get(Calendar.WEEK_OF_YEAR) - calendar.get(Calendar.WEEK_OF_YEAR)
        if (weekIndex < 0) throw IllegalArgumentException("The startDate mustn't be in the future")
        return weekIndex
    }
}
