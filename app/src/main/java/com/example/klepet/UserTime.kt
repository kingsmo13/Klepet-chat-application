package com.example.klepet

class UserTime {

    lateinit var user : User
    lateinit var time : String

    constructor(user: User, time: String) {
        this.user = user
        this.time = time
    }
}