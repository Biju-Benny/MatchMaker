package com.example.matchmaker.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matchmaker.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_matches.*
import kotlinx.android.synthetic.main.fragment_matches.view.*

private val TAG = "Matches"


class Matches : Fragment() {

    private lateinit var firestoreDB: FirebaseFirestore
    var usersILike: MutableList<User> = mutableListOf()
    var usersWhoLikeMe: MutableList<User> = mutableListOf()
    private lateinit var adapter: UserAdapterMAtchesOne
    var LikeCat: MutableList<LikeCatagory> = mutableListOf()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val uidCurrent = FirebaseAuth.getInstance().uid ?: ""

        firestoreDB = FirebaseFirestore.getInstance()
        val userReference = firestoreDB.collection("users").document(uidCurrent).collection("UsersILike")



        userReference.addSnapshotListener{snapshot, exception ->
            if (exception !=null || snapshot ==null){

                return@addSnapshotListener
            }

            var userAry = snapshot.toObjects(User::class.java)
            usersILike.clear()
            usersILike.addAll(userAry)
            adapter.notifyDataSetChanged()


        }

        val userReference2 = firestoreDB.collection("users").document(uidCurrent).collection("UsersWhoLikeMe")
        userReference2.addSnapshotListener{snapshot, exception ->
            if (exception !=null || snapshot ==null){

                return@addSnapshotListener
            }
            var userAry2 = snapshot.toObjects(User::class.java)
            usersWhoLikeMe.clear()
            usersWhoLikeMe.addAll(userAry2)
            adapter.notifyDataSetChanged()


        }



        LikeCat.clear()
        LikeCat.add(LikeCatagory("Users You like:",usersILike))
        LikeCat.add(LikeCatagory("Users Who Likes You:",usersWhoLikeMe))

        //Log.i(TAG,"users like list $LikeCat")





        val view = inflater.inflate(R.layout.fragment_matches, container, false)


        //usersILike = mutableListOf()
        //adapter= UserAdapterMAtchesOne(users)
        //view.rvUsersListScroll.isClickable =true

        //view.rvUsersListScroll.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL,false)
        //view.rvUsersListScroll.adapter = adapter
        val layoutManager : RecyclerView.LayoutManager= LinearLayoutManager(activity)

        view.rvUsersListScroll!!.layoutManager = layoutManager
        adapter = UserAdapterMAtchesOne(this, LikeCat!!)
        view.rvUsersListScroll!!.adapter = adapter


        return view




        // Inflate the layout for this fragment

    }




}