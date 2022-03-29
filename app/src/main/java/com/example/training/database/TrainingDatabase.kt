package com.example.training.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.training.database.dataClasses.BodyWeight
import com.example.training.database.dataClasses.Exercise
import com.example.training.database.dataClasses.blockData.Block
import com.example.training.database.dataClasses.blockData.BlockExercise
import com.example.training.database.dataClasses.blockData.BlockSet
import com.example.training.database.dataClasses.blockData.BlockTrainingDay
import com.example.training.database.dataClasses.trainingData.TrainingDay
import com.example.training.database.dataClasses.trainingData.TrainingExercise
import com.example.training.database.dataClasses.trainingData.TrainingSet


/**
 * class to access the database
 */
@Database(entities = [BodyWeight::class, Exercise::class, Block::class, BlockTrainingDay::class, BlockExercise::class, BlockSet::class, TrainingDay::class, TrainingExercise::class, TrainingSet::class],
          version = 5,
          exportSchema = false)
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
                        .fallbackToDestructiveMigration()
                        .build()
                }

                return instance
            }
        }
    }

    //todo the basic database structure is implemented but it is very bad. i want to improve it by drawing an er modell as well as using the normal forms to reduce redundency
}