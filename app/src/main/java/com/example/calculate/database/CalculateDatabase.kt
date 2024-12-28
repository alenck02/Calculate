package com.example.calculate.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.calculate.model.calculate

@Database(entities = [calculate::class], version = 1)
abstract class CalculateDatabase: RoomDatabase() {

    abstract fun getCalculateDao(): CalculateDao

    companion object{
        @Volatile
        private var instance: CalculateDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?:
        synchronized(LOCK){
            instance ?:
            createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CalculateDatabase::class.java,
                "calculate_db"
            )   .fallbackToDestructiveMigration()
                .build()
    }
}