package com.example.matchmaker.Fragments

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.matchmaker.MainActivity
import com.example.matchmaker.R
import com.example.matchmaker.User
import com.example.matchmaker.isUserSuccess
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user_details.*
import kotlinx.android.synthetic.main.member_list.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
//private const val TAG = "UserDetails"
class UserDetails : AppCompatActivity() {
    private lateinit var firestoreDB: FirebaseFirestore
    lateinit var userCrnt: User

    val db = Firebase.firestore
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        val uidExtra:String = intent.getStringExtra("UserId").toString()
        val firstNameExtra:String = intent.getStringExtra("FirstName").toString()
        val lastNAmeExtra:String = intent.getStringExtra("LastName").toString()
        val dobExtra:String = intent.getStringExtra("dob").toString()
        val locationExtra:String = intent.getStringExtra("location").toString()
        val ImgUrlExtra:String = intent.getStringExtra("imageUrl").toString()

        val age:Int = AgeClassfn(dobExtra)

        tvFirstName.text = firstNameExtra
        tvLastName.text = lastNAmeExtra
        tvAgeValue.text = age.toString()
        tvLocationValue.text = locationExtra
        //Glide.with(this).load(ImgUrlExtra).into(profilePic)

        val uidCurrent = FirebaseAuth.getInstance().uid ?: ""

        firestoreDB = FirebaseFirestore.getInstance()
        val userReference = firestoreDB
            .collection("users")
            .whereEqualTo("uid",uidCurrent)

        userReference.get().addOnSuccessListener {
            if(it.isEmpty){
                return@addOnSuccessListener
            }
            for (doc in it){
                userCrnt = doc.toObject(User::class.java)
            }


        }


        likeButton.setOnClickListener{

            val userILike = User(uidExtra, firstNameExtra, lastNAmeExtra, age.toString(), locationExtra , ImgUrlExtra)
            db.collection("users").document(uidCurrent).collection("UsersILike").document(uidExtra)
                .set(userILike)
                .addOnCompleteListener {
                    //Log.d(TAG,"User saved to database")

                }
                .addOnFailureListener {
                    //Log.d(TAG, "Failed to set value to database")
                }


            db.collection("users").document(uidExtra).collection("UsersWhoLikeMe").document(uidCurrent)
                .set(userCrnt)
                .addOnCompleteListener {
                    //Log.d(TAG,"User saved to database")

                }
                .addOnFailureListener {
                    //Log.d(TAG, "Failed to set value to database")
                }
        }







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