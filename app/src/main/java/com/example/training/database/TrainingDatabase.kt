package com.example.training.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.training.database.dataClasses.BodyWeight
import com.example.training.database.dataClasses.Exercise


/**
 * class to access the database
 */
@Database(entities = [BodyWeight::class, Exercise::class], version = 2, exportSchema = false)
@TypeConverters(DataConverter::class)
internal abstract class TrainingDatabase : RoomDatabase() {
    abstract val databaseDao: DatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: TrainingDatabase? = null

        fun getInstance(context: Context): TrainingDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, TrainingDatabase::class.java, "training_database_database")
                        .fallbackToDestructiveMigration().build()
                }

                return instance
            }
        }
    }
}