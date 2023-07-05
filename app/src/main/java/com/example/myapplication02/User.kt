package com.example.myapplication02

import android.widget.EditText
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "user_name")
    val name: String,

    @ColumnInfo(name="user_weight")
    val weight:Int,

    @ColumnInfo(name="user_height")
    val height:Int,

    @ColumnInfo(name="user_gender")
    val gender:String,

    @ColumnInfo(name="user_age")
    val age: Int,

    @ColumnInfo(name="user_bmr")
    val bmr: Int?


):Serializable