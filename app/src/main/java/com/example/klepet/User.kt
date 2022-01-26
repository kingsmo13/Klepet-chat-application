package com.example.klepet

import java.net.URI

class User {

    lateinit var uid : String
    lateinit var userName : String
    lateinit var uriString : String
    lateinit var phoneNumber: String

    constructor(uid: String, userName: String, uriString: String, phoneNumber: String) {
        this.uid = uid
        this.userName = userName
        this.uriString = uriString
        this.phoneNumber = phoneNumber
    }
    constructor()

}