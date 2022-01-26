package com.example.klepet

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.user_list_view.view.*

class UserListRecyclerView(var context: Context, var userList : ArrayList<User>) : RecyclerView.Adapter<UserListRecyclerView.UserViewHolder>() {


    class UserViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var textId = view.mUserName
        var imageId = view.mProfileImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.user_list_view, parent, false)

        return UserListRecyclerView.UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.textId.text = userList[position].userName

        Glide.with(context).load(userList[position].uriString)
            .placeholder(R.drawable.profile_pic)
            .into(holder.imageId)

        holder.textId.setOnClickListener {
            val intent = Intent(context , ChatActivity::class.java)
            intent.putExtra("name" , userList[position].userName)
            intent.putExtra("uid",userList[position].uid)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userList.size
    }

}