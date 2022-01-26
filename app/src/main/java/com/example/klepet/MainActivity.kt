package com.example.klepet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private lateinit var userList : ArrayList<User>
    private lateinit var dbRef : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var adaptor :  UserListRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userList = ArrayList()
        dbRef = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        adaptor = UserListRecyclerView(this , userList)

        //todo -> fetch users from userTime data

        dbRef.child("Users").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(postSnapshot in snapshot.children)
                {
                    var current = postSnapshot.getValue(User::class.java)

                    if(current!!.uid != auth.uid)
                    userList.add(current!!)
                }
                //notify data set changed

                //sort the list

                adaptor.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity , "Cannot access Chats",Toast.LENGTH_SHORT).show()
            }
        })

        signOutAgain.setOnClickListener {

            auth.signOut()
            val intent = Intent(this , OTPVerification::class.java)
            startActivity(intent)
            finish()

        }
        UserListRecycler.layoutManager = LinearLayoutManager(this)
        UserListRecycler.adapter = adaptor

        //glide
    }
}