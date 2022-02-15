package com.example.matchmaker

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_user_info.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log

private const val PICK_IMAGE_CODE = 1234
private const val TAG = "UserInfo"
const val EXTRA_USERNAME ="EXTRA_USERNAME"


var isUserSuccess = 0
class UserInfo : AppCompatActivity() {

    val curentUser = auth.currentUser

    private lateinit var tvAge: TextView
    private var photoUri: Uri? = null
    private lateinit var location: String
    //private lateinit var firestoreDb: FirebaseFirestore
    val db = Firebase.firestore
    private lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)


        

        spLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               location = adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        tvAge = findViewById(R.id.tvAge)
        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateLabel(myCalendar)

        }
        tvAge.setOnClickListener{
            DatePickerDialog(this,datePicker,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        profilePic.setOnClickListener{
            val imagePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            imagePickerIntent.type = "image/*"
            if (imagePickerIntent.resolveActivity(packageManager) != null){
                startActivityForResult(imagePickerIntent,PICK_IMAGE_CODE)
            }
        }

        btnSubmit.setOnClickListener{
            handleSubmit()
        }


    }





    private fun handleSubmit() {
        if (etFirstName.text.isBlank()){
            Toast.makeText(this, "Enter First name", Toast.LENGTH_SHORT).show()
            return
        }
        if(tvAge.text.isBlank()){
            Toast.makeText(this, "Enter Your Date Of Birth", Toast.LENGTH_SHORT).show()
            return

        }
        if(location == "Select your Location"){
            Toast.makeText(this, "Select your Location", Toast.LENGTH_SHORT).show()
            return
        }

        if(photoUri == null){
            Toast.makeText(this, "Upload image", Toast.LENGTH_SHORT).show()
            return

        }

        //btnSubmit.isEnabled = false

        //Toast.makeText(this, "All details uploaded", Toast.LENGTH_SHORT).show()
        //upload the collected details to firestore
        val photoUploadUri = photoUri as Uri
        val userIdenity = curentUser?.uid
        val photoRef = FirebaseStorage.getInstance().getReference("/images/$userIdenity-profilePic.jpeg")



        photoRef.putFile(photoUploadUri)
            .addOnSuccessListener{
                //btnSubmit.isEnabled =true
                photoRef.downloadUrl.addOnSuccessListener {


                    saveUserToFirebase(it.toString())

                }

            }
            .addOnFailureListener{
                Log.d(TAG,"Failed to upload Profile Pic")
            }

    }

    private fun saveUserToFirebase(photoUriString: String) {
        val dob = tvAge.text.toString()
        val name = etFirstName.text.toString()
        val SecondName = etLastName.text.toString()
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val userIdenity = curentUser?.uid
        //val ref = FirebaseDatabase.getInstance().getReference("/users/$userIdenity")
        val user = User(uid, name, SecondName, dob, location , photoUriString)

        //val user = hashMapOf("uid" to uid,"First Name" to name,"Second Name" to SecondName,"dob" to dob,"Location" to location, "ProfilePic" to photoUriString)

        db.collection("users").document(uid)
            .set(user)
            .addOnCompleteListener {
                Log.d(TAG,"User saved to database")
                isUserSuccess = 1
                val UserInfoIntent = Intent(this,MainActivity::class.java)
                //UserInfoIntent.putExtra(EXTRA_USERNAME,uid)
                startActivity(UserInfoIntent)
                finish()
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database")
            }

    }

    private fun updateLabel(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat,Locale.UK)
        tvAge.setText(sdf.format(myCalendar.time))


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_CODE){
            if (resultCode == Activity.RESULT_OK){
                //photoUri is the location of the photo selected
                photoUri = data?.data
                profilePic.setImageURI(photoUri)

            }else{
                Toast.makeText(this, "Image pick canceled", Toast.LENGTH_SHORT).show()
            }


        }

    }
}

//data class User(var Uid:String, var FirstName: String, var SecondName: String, var dob: String, var Location: String, var profilePicUrl : String)