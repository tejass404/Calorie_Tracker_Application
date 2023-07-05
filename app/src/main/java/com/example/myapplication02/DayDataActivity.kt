package com.example.myapplication02

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class DayDataActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_data)
        val userName = intent.getStringExtra("userName")?:""
        val date = intent.getStringExtra("Date")?:""
        val sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        sharedViewModel.Name = userName
        sharedViewModel.Date = date
        val cout: String? = intent.getStringExtra("Cout")
        val cin = intent.getStringExtra("Cin")
        Log.d(TAG,"$cin")
        val bmr = intent.getStringExtra("Bmr")
        val netcalorie = intent.getStringExtra("NetCal")
        Log.d(ContentValues.TAG,"Day data activity$userName")
        val cintv = findViewById<TextView>(R.id.cinTV)
        val couttv = findViewById<TextView>(R.id.coutTV)
        val bmrtv = findViewById<TextView>(R.id.BmrTV)
        val netcalorietv = findViewById<TextView>(R.id.netcalorieTV)
        cintv.setText(cin)
        couttv.setText(cout)
        bmrtv.setText(bmr)
        netcalorietv.setText(netcalorie)



        val navHostFragment = supportFragmentManager.findFragmentById(R.id.DataFrag) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.FoodFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation2)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_add_food -> {
                    navController.navigate(R.id.FoodFragment, bundleOf("userName" to userName))
                    true
                }
                R.id.nav_add_activity -> {
                    navController.navigate(R.id.ActivityFragment, bundleOf("userName" to userName))
                    true
                }
                else -> false
            }
        }
    }
}