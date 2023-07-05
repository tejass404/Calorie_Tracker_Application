package com.example.myapplication02

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class viewRVAdapter(private val context: Context, private var summary: MutableList<SummarizedViewData>, name:String): RecyclerView.Adapter<viewRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewRVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_list, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return summary.size
    }

    fun updateData(newSummary: MutableList<SummarizedViewData>) {
        summary = newSummary
        notifyDataSetChanged()
    }
    private fun formatDate(date: String): String {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        return outputFormat.format(parsedDate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val summarizedViewData = summary[position]
        holder.dateView.text = formatDate(summarizedViewData.date)
//        holder.dateView.text = summarizedViewData.date
        holder.cout.text = summarizedViewData.totalCalorieOut.roundToInt().toString()
        holder.cin.text = summarizedViewData.totalCalorieIn.toString()
        holder.bmr.text = summarizedViewData.bmr.toString()
        val netCalorie = summarizedViewData.totalCalorieIn - summarizedViewData.bmr - summarizedViewData.totalCalorieOut
        holder.netCalorie.text = netCalorie.roundToInt().toString()
        holder.viewData.setOnClickListener {
            val username=summarizedViewData.name
            val date=summarizedViewData.date
            val cout=summarizedViewData.totalCalorieOut
            val cin=summarizedViewData.totalCalorieIn
            val bmr=summarizedViewData.bmr

            val intent = Intent(context, DayDataActivity::class.java)
            intent.putExtra("userName",username)
            intent.putExtra("Date",date)
            intent.putExtra("Cout",String.format("%.2f", cout).toDouble().toString())
            intent.putExtra("Cin",String.format("%.2f", cin).toDouble().toString())
            intent.putExtra("Bmr",bmr.toString())
            intent.putExtra("NetCal",String.format("%.2f", netCalorie).toDouble().toString())
            context.startActivity(intent)
            Log.d(ContentValues.TAG, "adapter view data button pressed $username $cout")
        }

    }


    class ViewHolder(val View: View) : RecyclerView.ViewHolder(View) {
        val dateView: TextView = itemView.findViewById(R.id.dateTV)
        val cout: TextView = itemView.findViewById(R.id.coutTV)
        val cin: TextView = itemView.findViewById(R.id.cinTV)
        val bmr: TextView = itemView.findViewById(R.id.BmrTV)
        val netCalorie: TextView = itemView.findViewById(R.id.netcalorieTV)
        val viewData: Button = itemView.findViewById(R.id.viewDataBtn2)
    }


}
