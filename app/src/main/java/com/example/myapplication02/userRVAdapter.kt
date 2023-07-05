package com.example.myapplication02

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class userRVAdapter (private val context: Context, private val allUsers: MutableList<User> ): RecyclerView.Adapter<userRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userRVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_list, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: userRVAdapter.ViewHolder, position: Int) {
        val user = allUsers[position]

        holder.nameView.text = user.name
        holder.weightView.text = user.weight.toString()
        holder.heightView.text = user.height.toString()
        holder.ageView.text = user.age.toString()
        holder.genderView.text = user.gender
        holder.bmrView.text = user.bmr.toString()

        holder.deleteData.setOnClickListener {
            deleteItem(user, user.name)



        }
        holder.addData.setOnClickListener {
            val username=user.name
            val intent = Intent(context, addDataActivity::class.java)
            intent.putExtra("userName",username)

            context.startActivity(intent)
            Log.d(ContentValues.TAG, "adapter add data button pressed $username")

        }
        holder.viewData.setOnClickListener {
            val username=user.name
            val intent = Intent(context, ViewActivity::class.java)
            intent.putExtra("userName",username)

            context.startActivity(intent)
            Log.d(ContentValues.TAG, "adapter add data button pressed $username")
        }
    }




    override fun getItemCount(): Int {
        return allUsers.size

    }

    class ViewHolder(val userView: View) : RecyclerView.ViewHolder(userView) {
        val nameView: TextView = itemView.findViewById(R.id.userNameTV)
        val weightView: TextView = itemView.findViewById(R.id.weightTV)
        val heightView: TextView = itemView.findViewById(R.id.heightTV)
        val ageView: TextView = itemView.findViewById(R.id.ageTV)
        val genderView: TextView = itemView.findViewById(R.id.genderTV)
        val bmrView: TextView = itemView.findViewById(R.id.bmrTV)
        val viewData: Button = itemView.findViewById(R.id.viewDataBtn)
        val addData: Button = itemView.findViewById(R.id.addDataBtn)
        val deleteData: ImageButton = itemView.findViewById((R.id.deleteDataBtn))
    }

    private fun deleteItem(user: User, name: String) {
        val database = AppDataBase.getInstance(context)
        val dao = database.userDao()
        val foodDao = database.foodDao()
        val activityDao = database.activityDao()

        database.runInTransaction {
            dao.deleteUser(user)
            activityDao.deleteByName(name)
            foodDao.deleteByName(name)
        }
        val position = allUsers.indexOf(user)
        allUsers.removeAt(position)
        notifyItemRemoved(position)


    }
}

