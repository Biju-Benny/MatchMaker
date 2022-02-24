package com.example.matchmaker

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.matchmaker.Fragments.HomeFragment

import com.example.matchmaker.Fragments.UserDetails
import kotlinx.android.synthetic.main.member_list.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH

//this adapter help us attach the user list cards in the Home fragment
private const val TAG: String = "UserAdapter"


class UserAdapter(val fragment: Fragment, val users: List<User>, val onListClickListner: OnListClickListner):
    RecyclerView.Adapter<UserAdapter.ViewHolder>(){





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.member_list,parent,false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(users[position])
        //var iUid = users[position].uid
        holder.itemView.setOnClickListener{
            onListClickListner.onListItemClickLIstner(position)
        }


    }

    override fun getItemCount()= users.size



    inner class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(user: User) {

            val age = AgeClassfn(user.dob)

            itemView.tvFirstNameList.text = user.firstName
            itemView.tvAgeList.text = age.toString()
            itemView.tvLocationList.text = user.location

            //Glide.with(fragment).load(user.profilePicUrl).into(itemView.listIV)



        }




        @RequiresApi(Build.VERSION_CODES.O)
        private fun AgeClassfn(dob: String): Int {

            var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            var dateOfBirth = LocalDate.parse(dob, formatter)

            val userYear: Int = dateOfBirth.year
            val userMonth:Int = dateOfBirth.monthValue
            val userDate: Int = dateOfBirth.dayOfMonth

            var myCalendar = Calendar.getInstance()
            val curentDate:Int = myCalendar.get(Calendar.DAY_OF_MONTH)
            val currentMonth: Int = myCalendar.get(Calendar.MONTH)
            val currentYear: Int = myCalendar.get(Calendar.YEAR)

            var Age: Int = 0
            Log.i(TAG,"user: $userDate , $userMonth , $userYear, current: $curentDate , $currentMonth , $currentYear")


            if( userMonth > currentMonth) {
                Age = currentYear-userYear-1
                return Age
            }
            if (userMonth == currentMonth){
                if(userDate>curentDate){
                    Age = currentYear-userYear-1
                    return Age

                }
                Age = currentYear-userYear
                return Age
            }
            else{
            Age = currentYear-userYear
            return Age}

        }
    }

}