package com.example.myapplication02

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ActivityData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name="name")
    val name:String?,

    @ColumnInfo(name = "activity_date")
    val date: String,

    @ColumnInfo(name="activity")
    val activity:String,

    @ColumnInfo(name="activity_met")
    val met:Double?,

    @ColumnInfo(name="activity_duration")
    val duration: Double?,

    @ColumnInfo(name="activity_calorieOut")
    val calorieOut: Double,

    @ColumnInfo(name="activity_bmr")
    val bmr: Int?,

    @ColumnInfo(name="activity_name")
    val activityName: String?


): Serializable
