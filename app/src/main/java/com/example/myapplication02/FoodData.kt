package com.example.myapplication02

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class FoodData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name="name")
    val name:String,

    @ColumnInfo(name = "food_date")
    val date: String,

    @ColumnInfo(name="food_meal")
    val mealType:String,

    @ColumnInfo(name="food_food")
    val Food:String,

    @ColumnInfo(name="food_serving")
    val serving: Double?,

    @ColumnInfo(name="food_calorieIn")
    val calorieIn: Double,

    @ColumnInfo(name="food_bmr")
    val bmr: Int?,

    @ColumnInfo(name="food_group")
    val foodGroup: String?


):Serializable
