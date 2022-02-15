package com.example.matchmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WelcomeScreen : AppCompatActivity() {
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)


        val uid = intent.getStringExtra("userId")
        val userRf = db.collection("users").document(uid!!)

        //Log.e(LoginActivity.TAG,"userRef is: $userRf")

        if (uid == null){
            return
        }
        userRf.get()
            .addOnSuccessListener { document->
                if (document.data != null ){
                    //Log.d(LoginActivity.TAG, "DocumentSnapshot data: ${document.data}")

                    startActivity(Intent(this, MainActivity::class.java))

                    finish()

                }else{
                    startActivity(Intent(this, UserInfo::class.java))
                    finish()

                }


            }.addOnFailureListener { exception ->
                //intentFunctionUserinfo()
                //finish()
                //Log.d(LoginActivity.TAG, "get failed with ", exception)
            }
    }
}