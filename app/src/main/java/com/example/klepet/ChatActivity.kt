package com.example.klepet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    var adaptor : ChatAdapter? = null
    private lateinit var messageList : ArrayList<Chat>
    private lateinit var receiverId : String
    private lateinit var senderId : String
    private lateinit var dbRef : DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messageList = ArrayList()
        adaptor = ChatAdapter(this , messageList)
        receiverId = intent.getStringExtra("uid")!!
        senderId = FirebaseAuth.getInstance().currentUser!!.uid
        dbRef = FirebaseDatabase.getInstance().reference
        adaptor = ChatAdapter(this , messageList)


        dbRef.child("chats").child(senderId).child(receiverId).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for(postSnapShot in snapshot.children)
                {
                    val chat = postSnapShot.getValue(Chat::class.java)
                    messageList.add(chat!!)
                }
                adaptor!!.notifyDataSetChanged()

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = adaptor


        sendBTN.setOnClickListener {
            val message = messageBOX.text.toString()
            sendMessage(message)
            messageBOX.setText("Type your message")
        }



    }

    fun sendMessage(message : String)
    {

        val chatObject = Chat("" , message , senderId ,receiverId )

        dbRef.child("chats").child(senderId).child(receiverId).push().setValue(chatObject)

        dbRef.child("chats").child(receiverId).child(senderId).push().setValue(chatObject)
    }
}

