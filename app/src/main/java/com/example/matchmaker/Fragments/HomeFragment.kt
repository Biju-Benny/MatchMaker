package com.example.matchmaker.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matchmaker.OnListClickListner
import com.example.matchmaker.R
import com.example.matchmaker.User
import com.example.matchmaker.UserAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

private val TAG = "HomeFragment"
class HomeFragment : Fragment(), OnListClickListner {

    private lateinit var firestoreDB:FirebaseFirestore
    private lateinit var users: MutableList<User>
    private lateinit var adapter: UserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val uidCurrent = FirebaseAuth.getInstance().uid ?: ""

        firestoreDB = FirebaseFirestore.getInstance()
        val userReference = firestoreDB
            .collection("users")
            .whereNotEqualTo("uid",uidCurrent)
            .limit(20)



        userReference.addSnapshotListener{snapshot, exception ->
            if (exception !=null || snapshot ==null){

                return@addSnapshotListener
            }
            var userAry = snapshot.toObjects(User::class.java)
            users.clear()
            users.addAll(userAry)
            adapter.notifyDataSetChanged()

        }



        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        users = mutableListOf()
        adapter= UserAdapter(this,users, this)
        view.rvUsersList.isClickable =true
        view.rvUsersList.adapter = adapter
        view.rvUsersList.layoutManager = LinearLayoutManager(activity)


        return view




    }

    override fun onListItemClickLIstner(position: Int) {
        val intent = Intent(context,UserDetails::class.java)
        intent.putExtra("UserId", users[position].uid)
        intent.putExtra("FirstName", users[position].firstName)
        intent.putExtra("LastName", users[position].secondName)
        intent.putExtra("dob",users[position].dob)
        intent.putExtra("location",users[position].location)
        intent.putExtra("imageUrl",users[position].profilePicUrl)
        startActivity(intent)
    }


    //override fun onViewCreated(view: View, savedInstanceState: Bundle?) {        super.onViewCreated(view, savedInstanceState) }



}




