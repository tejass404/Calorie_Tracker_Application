package com.example.myapplication02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val userRecyclerView= findViewById<RecyclerView>(R.id.userRV)





        val db = AppDataBase.getInstance(this)
        val allUsers= db.userDao().getAll()
        userRecyclerView.apply {
            layoutManager= LinearLayoutManager(this@MainActivity2)
            adapter= userRVAdapter(this@MainActivity2,allUsers)

        }







    }
}