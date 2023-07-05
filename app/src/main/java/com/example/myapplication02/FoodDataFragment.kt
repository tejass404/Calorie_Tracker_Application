package com.example.myapplication02

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private lateinit var foodRecyclerView: RecyclerView


class FoodDataFragment : Fragment() {



    private var datetv: TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val name = sharedViewModel.Name
        val date = sharedViewModel.Date
        Log.d(TAG,"#############")



        Log.d(ContentValues.TAG,"fooddata fragment$name")
        val db = AppDataBase.getInstance(requireContext())
        val foodListdata= db.foodDao().getAll(name,date)
        val adapter = foodListdata?.let { foodRVAdapter(it) }
        datetv?.setText(date)

        foodRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        foodRecyclerView.adapter =adapter

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_food_data, container, false)
        foodRecyclerView = view.findViewById<RecyclerView>(R.id.foodRV)
        datetv=view.findViewById<TextView>(R.id.DateTV)

        return view

    }

}
class foodRVAdapter(val foodListdata: MutableList<FoodData>) :RecyclerView.Adapter<foodRVAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): foodRVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: foodRVAdapter.ViewHolder, position: Int) {
        val foodListData = foodListdata[position]
        holder.mealTypeTV.text=foodListData.mealType
        holder.servingTV.text= foodListData.serving.toString()
        holder.calorieInTV.text= foodListData.calorieIn.toString()
        holder.foodGrouptv.text=foodListData.foodGroup
    }

    override fun getItemCount(): Int {
        return foodListdata.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealTypeTV: TextView = itemView.findViewById(R.id.mealTypeTV)
        val servingTV: TextView = itemView.findViewById(R.id.servingTV)
        val calorieInTV: TextView = itemView.findViewById(R.id.CalorieinTV)
        val foodGrouptv: TextView=itemView.findViewById(R.id.foodGroupTV)
    }

}