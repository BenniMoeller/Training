package com.example.training.database.repositories

import com.example.training.database.DatabaseDao
import com.example.training.database.dataClasses.BodyWeight

/**
 * class that acts as access to the Database for the BodyWeights
 */
internal class BodyWeightRepository(private val dataBaseDao: DatabaseDao) {

    /**
     * saves a bodyWeight to the Database
     * @param bodyWeight BodyWeight the weight saved to the database
     * @return the id of the bodyWeight saved in the database
     */
    fun saveBodyWeight(bodyWeight: BodyWeight) = dataBaseDao.saveBodyWeight(bodyWeight)

    /**
     * gets all bodyWeights from the Database
     * @return LiveData<List<BodyWeight>> the bodyweights in a list in a livedata object
     */
    fun getAllBodyWeights() = dataBaseDao.getAllBodyWeights()

}