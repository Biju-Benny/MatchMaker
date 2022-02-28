package com.example.matchmaker.Fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.matchmaker.EditUserDetails
import com.example.matchmaker.R
import com.example.matchmaker.User
import com.example.matchmaker.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user_details.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*



class Profile : Fragment() {

    private lateinit var firestoreDB: FirebaseFirestore
    private lateinit var userCurrent: User






    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val uidCurrent = FirebaseAuth.getInstance().uid ?: ""
        firestoreDB = FirebaseFirestore.getInstance()
        val userReference = firestoreDB
            .collection("users")
            .whereEqualTo("uid",uidCurrent)

        userReference.get().addOnSuccessListener {
            if (it.isEmpty){
                return@addOnSuccessListener
            }
            for (doc in it){
                userCurrent = doc.toObject(User::class.java)
                val dob:String = userCurrent.dob
                var age:String = AgeClassfn(dob).toString()

                tvFirstNameProfile.text = userCurrent.firstName
                tvLastNameProfile.text= userCurrent.secondName
                tvAgeValueProfile.text = age
                tvLocationValueProfile.text = userCurrent.location
                Glide.with(this).load(userCurrent.profilePicUrl).into(profilePicProfile)
                likeButtonProfile.setOnClickListener{

                    intentFunction()
                }


            }
        }






        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)



    }

    private fun intentFunction() {
        val intent = Intent(context,UserInfo::class.java)
        startActivity(intent)
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
        //Log.i(com.example.matchmaker.TAG,"user: $userDate , $userMonth , $userYear, current: $curentDate , $currentMonth , $currentYear")


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