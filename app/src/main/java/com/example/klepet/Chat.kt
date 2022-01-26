package com.example.klepet

class Chat {

    lateinit var timeStamp : String
    lateinit var message : String
    lateinit var senderId : String
    lateinit var receiverId : String



    constructor(timeStamp: String, message: String , senderId : String , receiverId : String) {
        this.timeStamp = timeStamp
        this.message = message
        this.senderId = senderId
        this.receiverId = receiverId
    }

    constructor()
}