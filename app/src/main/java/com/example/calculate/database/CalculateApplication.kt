package com.example.calculate.database

import android.app.Application
import androidx.room.Room

class CalculateApplication : Application() {
    lateinit var database: CalculateDatabase

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            CalculateDatabase::class.java,
            "calculate_database"
        ).build()
    }
}