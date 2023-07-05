package com.example.myapplication02

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private var selectedMeal: String = ""
private lateinit var dateEdt: EditText

val foodList: MutableList<AddFoodFragment.Food> = mutableListOf()
private lateinit var foodName: AutoCompleteTextView
private lateinit var servingEt: EditText
private lateinit var selectedFood: AddFoodFragment.Food
private var selectedFoodCalories: Double? = null
private var selectedFoodGroup: String? = ""



class AddFoodFragment() : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_food, container, false)
//        AddFoodFragment(userName)

        dateEdt = view.findViewById(R.id.dateET)
        foodName = view.findViewById<AutoCompleteTextView>(R.id.foodNameET)
        var mealtype = view.findViewById<Spinner>(R.id.mealTypeET)
        servingEt = view.findViewById<EditText>(R.id.ServingET)
        var nextBtn=view.findViewById<Button>(R.id.nextBtn)
        val mealTypes = resources.getStringArray(R.array.meal_types)


        val adapterMeal = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mealTypes)
        adapterMeal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mealtype.adapter = adapterMeal

        mealtype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedMeal = mealTypes[position]
//                selectedFoodName = foodList[position].name
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Please select a meal", Toast.LENGTH_SHORT).show()

            }
        }

        val json = readJsonFileFromAssets("food-calories.json")
        parseFoodData(json)

        val adapterFood = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, foodList.map { it.name })
        foodName.setAdapter(adapterFood)


        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val name = sharedViewModel.userName
        Log.d(TAG, "add food fragment $name")



        foodName.setOnItemClickListener { parent, _, position, _ ->
            val selectedFoodName = foodName.text.toString()
            val foodPosition = foodList.indexOfFirst { it.name == selectedFoodName }
            if (foodPosition != -1) {
                selectedFood = foodList[foodPosition]
                selectedFoodCalories = selectedFood.Calories.toDoubleOrNull()
                selectedFoodGroup= selectedFood.foodGroup
                Log.d(TAG,"$selectedFoodCalories $selectedFood $selectedFoodGroup")
            }


        }

        dateEdt.setOnClickListener {
            datePicker()
        }
        val navController = findNavController()
        nextBtn.setOnClickListener {
            val db = AppDataBase.getInstance(requireContext())
            if (name != null) {
                val bmr= name?.let { db.userDao().getBMRByName(it) }
                insertDataIntoDatabase(name,navController, bmr)


            }


        }

        return view


    }
    private fun insertDataIntoDatabase(name: String, navController: NavController, bmr: Int?) {
        val selectedDate = dateEdt.text.toString()
        val servingValue = servingEt.text.toString().toDoubleOrNull()
        val selectedFoodName = foodName.text.toString()
//        val calories = selectedFood.calories.toDoubleOrNull()
        Log.d(TAG, "Database:  $selectedDate $selectedFoodName $servingValue $selectedFoodCalories $name $selectedMeal")


        if (selectedDate.isNotEmpty() && selectedFoodName.isNotEmpty() && servingValue != null && selectedFoodCalories != null) {
            val calculatedCalories = selectedFoodCalories!! * servingValue

            val foodData = FoodData(name =name, date =selectedDate, mealType = selectedMeal, Food = selectedFoodName, serving = servingValue, calorieIn =  calculatedCalories, bmr = bmr, foodGroup = selectedFoodGroup)
                val appDatabase = AppDataBase.getInstance(requireContext())
            val foodDao = appDatabase.foodDao()
            foodDao.insertFoodData(foodData)
            navController.navigate(R.id.addActivityFragment)
            Toast.makeText(requireContext(), "Data inserted into database", Toast.LENGTH_SHORT).show()
        } else {
            val db = AppDataBase.getInstance(requireContext())

            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()

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
        val foodArray = gson.fromJson(json, Array<Food>::class.java)
        foodList.addAll(foodArray)
    }

    data class Food(
        val ID: String,
        val name: String,
        val foodGroup: String,
        val Calories: String,
        val fat: String,
        val protein: String,
        val carbohydrate: String,
        val servingDescription: String
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



