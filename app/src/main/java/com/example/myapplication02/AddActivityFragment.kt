package com.example.myapplication02

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private lateinit var dateEdt: EditText
private lateinit var activityName: AutoCompleteTextView
private lateinit var metvalue: TextView
private lateinit var duration: EditText
private val activityList: MutableList<AddActivityFragment.Activity> = mutableListOf()
private var selectedActivityMET: Double?=null
private var selectedActivityGroup:String = ""


class AddActivityFragment() : Fragment() {

    private lateinit var selectedActivity: Activity

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_activity, container, false)
        dateEdt = view.findViewById(R.id.dateactET)
        activityName = view.findViewById(R.id.activityET)
        metvalue = view.findViewById(R.id.metTV)
        duration = view.findViewById(R.id.durationET)
        val savebtn = view.findViewById<Button>(R.id.saveBtn)

        dateEdt.setOnClickListener {
            datePicker()
        }

        val json = readJsonFileFromAssets("MET-values.json")
        parseFoodData(json)
        val adapterActivity = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            activityList.map { it.SPECIFICMOTION })
        activityName.setAdapter(adapterActivity)
        activityName.setOnItemClickListener { parent, _, position, _ ->
            val selectedActivityName = activityName.text.toString()
            val activityPosition =
                activityList.indexOfFirst { it.SPECIFICMOTION == selectedActivityName }
            if (activityPosition != -1) {
                selectedActivity = activityList[activityPosition]
                selectedActivityMET = selectedActivity.METs
                selectedActivityGroup=selectedActivity.ACTIVITY

                Log.d(
                    ContentValues.TAG,
                    "MET :::$selectedActivityName $selectedActivityMET $activityPosition"
                )
                metvalue.setText(selectedActivityMET.toString())
            }


        }
        duration.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val durationText = s.toString()
                val durationParts = durationText.split(":")
                if (durationParts.size == 2) {
                    val hours = durationParts[0].toDoubleOrNull()
                    val minutes = durationParts[1].toDoubleOrNull()
                    if (hours != null && minutes != null) {
                        val durationInHours = hours + (minutes / 60)
                        Log.d(ContentValues.TAG, "Duration::: $durationInHours hours")
                    }
                }
            }
        })
        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val name = sharedViewModel.userName

        savebtn.setOnClickListener {
            val db = AppDataBase.getInstance(requireContext())
            lifecycleScope.launch {
                val weight = name?.let { it1 -> db.userDao().getUserWeightByName(it1) }
//                val db = AppDataBase.getInstance(requireContext())
                val bmr= name?.let { db.userDao().getBMRByName(it) }

                insertActivityData(weight,name,bmr)
                val intent = Intent(requireContext(), MainActivity2::class.java)
                startActivity(intent)
            }
        }

            return view

    }
    private  fun insertActivityData(
        weight: Int?,
        name: String?,
        bmr: Int?
    ) {
        val date = dateEdt.text.toString()
        val met = selectedActivityMET
        val activityGroup= selectedActivityGroup
        val durationText = duration.text.toString()
        val durationParts = durationText.split(":")
        val hours = durationParts[0].toDoubleOrNull()
        val minutes = durationParts[1].toDoubleOrNull()
        val durationInHours = hours?.plus(minutes?.div(60.0) ?: 0.0)
        val bmr=bmr

        val calorieOut = met?.times(weight?.toDouble() ?: 0.0)?.times(durationInHours ?: 0.0)


        if ( date.isNotEmpty() && met != null && durationInHours != null && calorieOut != null) {
            val activityData = ActivityData(
                name = name,
                date = date,
                activity = selectedActivity.SPECIFICMOTION,
                met = met,
                duration = durationInHours,
                calorieOut = calorieOut,
                bmr= bmr,
                activityName = activityGroup
            )
            val appDatabase = AppDataBase.getInstance(requireContext())
            val activityDao = appDatabase.activityDao()
            activityDao.insertactivityData(activityData)
            Toast.makeText(
                requireContext(),
                "inserted",
                Toast.LENGTH_SHORT
            ).show()
            Log.d(ContentValues.TAG, "Activity Data:::$name $date ${selectedActivity.SPECIFICMOTION} $met $durationInHours $calorieOut")

        } else {
            Toast.makeText(
                requireContext(),
                "Please enter valid data",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun readJsonFileFromAssets(fileName: String): String {
        val inputStream = requireContext().assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer)
    }

    private fun parseFoodData(json: String) {
        val gson = Gson()
        val activityArray = gson.fromJson(json, Array<Activity>::class.java)
        activityList.addAll(activityArray)
    }

    data class Activity(
        val ACTIVITY: String,
        val SPECIFICMOTION: String,
        val METs: Double
    )
    private fun datePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
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
                        requireContext(),
                        "Please select a date within the allowed range",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val formattedDate =
                        SimpleDateFormat(
                            "dd-MM-yyyy",
                            Locale.getDefault()
                        ).format(selectedDate.time)
                    dateEdt.setText(formattedDate)
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


}

