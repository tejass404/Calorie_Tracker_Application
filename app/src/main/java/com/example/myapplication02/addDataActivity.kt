package com.example.myapplication02

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class addDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

        val userName = intent.getStringExtra("userName")?:""
        Log.d(ContentValues.TAG,"add data activity$userName")




        val navHostFragment = supportFragmentManager.findFragmentById(R.id.addDataFrag) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.addFoodFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_add_food -> {
                    navController.navigate(R.id.addFoodFragment, bundleOf("userName" to userName))
                    true
                }
                R.id.nav_add_activity -> {
                    navController.navigate(R.id.addActivityFragment, bundleOf("userName" to userName))
                    true
                }
                else -> false
            }
        }


        val sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        sharedViewModel.userName = userName

    }


}
