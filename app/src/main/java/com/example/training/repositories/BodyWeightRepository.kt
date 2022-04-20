package com.example.training.repositories

import com.example.training.database.DatabaseDao
import com.example.training.database.dataClasses.BodyWeight
import java.lang.IllegalArgumentException
import java.util.*

/**
 * class that acts as access to the Database for the BodyWeights
 */
internal class BodyWeightRepository(private val dataBaseDao: DatabaseDao) {

    /**
     * saves a bodyWeight to the Database
     * @param bodyWeight BodyWeight the BodyWeight saved to the database
     * @return the id of the bodyWeight saved in the database
     */
    fun saveBodyWeight(bodyWeight: BodyWeight) = dataBaseDao.saveBodyWeight(bodyWeight)

    /**
     * gets all bodyWeights from the Database
     * @return LiveData<List<BodyWeight>> the bodyweights in a list in a livedata object
     */
    fun getAllBodyWeights() = dataBaseDao.getAllBodyWeights()

    /**
     * deletes a BodyWeight from the dataBase
     * @param bodyWeight the bodyWeight to be deleted
     */
    fun deleteBodyWeight(bodyWeight: BodyWeight) {
        val wasDeleted = dataBaseDao.deleteBodyWeight(bodyWeight)
        if (wasDeleted == 0) throw IllegalArgumentException("bodyWeight was not found in the dataBase")
    }

    /**
     * retrieves a BodyWeight from the Database by its id
     * @param date Date the date of the BodyWeight to be retrieved
     * @return BodyWeight
     */
    fun getBodyWeightByDate(date: Date): BodyWeight {
        return dataBaseDao.getBodyWeightById(date) ?: throw IllegalArgumentException("no BodyWeight with that id was found in the dataBase")
    }

}