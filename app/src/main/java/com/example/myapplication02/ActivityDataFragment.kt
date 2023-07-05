package com.example.myapplication02

import android.annotation.SuppressLint
import android.content.ContentValues
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

class ActivityDataFragment : Fragment() {


    private var datetv:TextView? = null

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
        Log.d(ContentValues.TAG,"activitydata fragment$name")
        val db = AppDataBase.getInstance(requireContext())
        val activityListdata= db.activityDao().getAll(name,date)
        val adapter = activityRVAdapter(activityListdata)
        datetv?.setText(date)
        foodRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        foodRecyclerView.adapter =adapter

    }



    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_activity_data_frgment, container, false)
        foodRecyclerView = view.findViewById<RecyclerView>(R.id.activityRV)
        datetv=view.findViewById<TextView>(R.id.DateTV2)
        return view


    }


}
class activityRVAdapter(val activityListdata: MutableList<ActivityData>): RecyclerView.Adapter<activityRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): activityRVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: activityRVAdapter.ViewHolder, position: Int) {
        val activityListData = activityListdata[position]
        holder.activityTV.text=activityListData.activityName
        holder.descriptionTV.text=activityListData.activity
        holder.metTV.text= activityListData.met.toString()
        holder.durationTV.text= (String.format("%.2f",activityListData.duration)+" Hrs.")
        holder.calorieoutTV.text= String.format( "%.3f",activityListData.calorieOut)

    }


    override fun getItemCount(): Int {
        return activityListdata.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val activityTV: TextView = itemView.findViewById(R.id.activityTV)
        val descriptionTV: TextView = itemView.findViewById(R.id.descriptionTV)
        val metTV: TextView = itemView.findViewById(R.id.metTV)
        val durationTV: TextView=itemView.findViewById(R.id.durationTV)
        val calorieoutTV: TextView=itemView.findViewById(R.id.calorieoutTV)

    }


}