package com.example.training.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.training.database.dataClasses.BodyWeight

/**
 * the dao for the database
 */
@Dao
interface DatabaseDao {

    @Insert
    fun saveBodyWeight(bodyWeight: BodyWeight): Long

    @Query("SELECT * FROM bodyweight_table")
    fun getAllBodyWeights(): LiveData<List<BodyWeight>>

}