package com.example.calculate.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "calculate")
@Parcelize
data class calculate(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val expression: String,
    val answer: String
):Parcelable