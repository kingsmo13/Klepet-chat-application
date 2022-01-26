package com.example.klepet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    val handler : Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        handler.postDelayed(object : Runnable {
            override fun run() {
                val intent = Intent(this@SplashScreen , OTPVerification::class.java)
                 startActivity(intent)
            }

        },2000)


    }
}