package com.example.calculate.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.calculate.model.calculate

@Dao
interface CalculateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calculate: calculate)

    @Query("DELETE FROM calculate") // 모든 항목 삭제
    suspend fun deleteAll()

    @Query("SELECT * FROM calculate ORDER BY id DESC")
    fun getAll(): LiveData<List<calculate>>
}