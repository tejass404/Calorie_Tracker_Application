package com.example.myapplication02

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.room.Room
private var selectedGender: String = ""

class MainActivity : AppCompatActivity() {

    fun calcBMR(weight: Int, height: Int, age: Int,gender: String): Int {
        val Bmr:Int
        return if(gender=="male" || gender=="Male"){
            Bmr= (66.4730+(13.7513*weight)+(5.0033*height)-(6.7550*age)).toInt()
            Bmr
        } else if (gender=="female" || gender=="Female"){
            Bmr= (655.0955+(9.5634*weight)+(1.8496*height)-(4.6756*age)).toInt()
            Bmr
        } else
            0

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val etName= findViewById<EditText>(R.id.nameET)
        val etAge= findViewById<EditText>(R.id.ageET)
        val etWeight= findViewById<EditText>(R.id.weightET)
        val etHeight= findViewById<EditText>(R.id.heightET)
        val etGender= findViewById<Spinner>(R.id.genderET)
        val signUpBtn= findViewById<Button>(R.id.signUpBtn)
        val userListBtn=findViewById<Button>(R.id.userListBtn)

        val db = AppDataBase.getInstance(this)

        val genders = resources.getStringArray(R.array.genders)
        val adapterGender = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        etGender.adapter = adapterGender

        etGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedGender = genders[position]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@MainActivity, "Please select Gender", Toast.LENGTH_SHORT).show()
            }
        }
        Log.d(TAG,"$selectedGender")
        signUpBtn.setOnClickListener{
            val name= etName.text.toString()
            val weight= etWeight.text.toString().toInt()
            val height= etHeight.text.toString().toInt()
            val age = etAge.text.toString().toInt()
            val bmr:Int = calcBMR(weight,height,age, selectedGender)


            val user=User(name = name, weight = weight, height = height, gender = selectedGender, age = age, bmr= bmr)
            val userDao= db.userDao()
            userDao.insertAll(user)
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)

        }
        userListBtn.setOnClickListener{
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

    }
}