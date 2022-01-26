package com.example.klepet

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_phone_authentication.*

class PhoneAuthentication : AppCompatActivity() {

    private lateinit var phoneNumber : String
    private lateinit var selectedImage : Uri
    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_authentication)
        supportActionBar?.hide()

//        Log.i("mTag" , "Activity Launched")
        phoneNumber = intent.getStringExtra("UserNumber")!!
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        profile_image.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent , 1)
        }

        createAccount.setOnClickListener {

            //verify userName

            val storageRef  = FirebaseStorage.getInstance().getReference().child("profiles").child(phoneNumber)

            storageRef.putFile(selectedImage).addOnSuccessListener {

                if(it.task.isSuccessful)
                {
                    storageRef.downloadUrl.addOnSuccessListener {

                        val StringUri = it.toString()

                        val user = User(auth.uid!! , userName.text.toString() , StringUri , phoneNumber)
                        database.child("Users").child(auth.uid!!).setValue(user)
                        database.child("Phone Numbers").push().setValue(phoneNumber)

                        val intent = Intent(this , MainActivity::class.java)
                        startActivity(intent)
                        finish()

                        //jump to next Activity
                    }
                }
                else
                {
                    Toast.makeText(this , "Something went wrong" , Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data != null)
        {
            if(data.data != null)
            {
                profile_image.setImageURI(data.data!!)
                selectedImage = data.data!!
            }
        }
    }
}