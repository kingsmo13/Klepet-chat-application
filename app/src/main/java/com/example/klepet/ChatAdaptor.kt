package com.example.klepet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.receive_chat_message.view.*
import kotlinx.android.synthetic.main.sent_chat_message.view.*

class ChatAdapter(var context : Context, var messageList : ArrayList<Chat>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val SentId = 1
    val ReceivedId = 2

    class SentViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val sentTextBox = view.sentText
    }

    class ReceivedViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val receivedTextBox = view.receiveText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == SentId)
        {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.sent_chat_message, parent, false)

            return SentViewHolder(view)
        }else{
            val view = LayoutInflater.from(context)
                .inflate(R.layout.receive_chat_message, parent, false)

            return ReceivedViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val current = messageList[position]

        if(holder.javaClass == SentViewHolder::class.java)
        {
            val sentViewHolder = holder as SentViewHolder
            sentViewHolder.sentTextBox.text = current.message
        }else{

            val receivedViewHolder = holder as ReceivedViewHolder
            receivedViewHolder.receivedTextBox.text = current.message

        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val current = messageList[position]

        if(current.senderId.equals(FirebaseAuth.getInstance().currentUser!!.uid))
            return SentId;
        else return ReceivedId
    }


}