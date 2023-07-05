package com.example.myapplication02

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
private lateinit var formattedDate: String
class ViewActivity : AppCompatActivity() {
    private lateinit var userName: String
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var searchDateET: EditText
    private lateinit var name: TextView
    private lateinit var summary: MutableList<SummarizedViewData>
    private lateinit var adapter: viewRVAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        userName = intent.getStringExtra("userName") ?: ""
        userRecyclerView = findViewById(R.id.viewRV)
        searchDateET = findViewById(R.id.searchDateET)
        name = findViewById(R.id.nameTV)

        name.text = userName
        val db = AppDataBase.getInstance(this)
        summary = mutableListOf()

        val calorieInData = db.foodDao().getCalorieInActivityData(userName)
        summary.addAll(calorieInData)

        val calorieOutData = db.activityDao().getCalorieOutActivityData(userName)

        for (data in calorieOutData) {
            val existingData = summary.find { it.date == data.date }
            if (existingData != null) {
                existingData.totalCalorieOut = data.totalCalorieOut
            } else {
                summary.add(
                    SummarizedViewData(
                        name = data.name,
                        date = data.date,
                        totalCalorieIn = 0.0,
                        totalCalorieOut = data.totalCalorieOut,
                        bmr = data.bmr
                    )
                )
            }
        }

        for (data in summary) {
            val existingCalorieOutData = calorieOutData.find { it.date == data.date }
            if (existingCalorieOutData == null) {
                data.totalCalorieOut = 0.0
            }
        }
        val sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        sharedViewModel.summaryList =summary

        adapter = viewRVAdapter(this, summary, userName)
        userRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ViewActivity)
            adapter = this@ViewActivity.adapter
        }

        searchDateET.setOnClickListener {
            datePicker()
        }
    }


        private fun datePicker() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val selectedDate = Calendar.getInstance().apply {
                        set(selectedYear, selectedMonth, selectedDayOfMonth)
                    }
                    val minDate = Calendar.getInstance().apply {
                        add(Calendar.MONTH, -1)
                    }
                    val maxDate = Calendar.getInstance().apply {
                        add(Calendar.MONTH, 1)
                    }

                    if (selectedDate.before(minDate) || selectedDate.after(maxDate)) {
                        Toast.makeText(
                            this,
                            "Please select a date within the allowed range",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                         formattedDate =
                            SimpleDateFormat(
                                "dd-MM-yyyy",
                                Locale.getDefault()
                            ).format(selectedDate.time)
                        searchDateET.setText(formattedDate)
                        filterSummaryByDate()
                    }
                },
                year, month, day
            )
            val minDate = Calendar.getInstance().apply {
                add(Calendar.MONTH, -1)
            }.timeInMillis
            val maxDate = Calendar.getInstance().apply {
                add(Calendar.MONTH, 1)
            }.timeInMillis
            datePickerDialog.datePicker.minDate = minDate
            datePickerDialog.datePicker.maxDate = maxDate
            datePickerDialog.show()

        }

    private fun filterSummaryByDate() {
        val filteredSummary = if (formattedDate.isNotEmpty()) {
            summary.filter { it.date == formattedDate }.toMutableList()
        } else {
            summary.toMutableList()
        }
        adapter.updateData(filteredSummary)
    }





}
