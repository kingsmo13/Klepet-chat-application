package com.example.klepet

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_otpverification.*
import java.util.concurrent.TimeUnit
import com.google.firebase.database.DataSnapshot
import com.google.firebase.ktx.Firebase

class OTPVerification : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    public var alreadyExist : Boolean  = false
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpverification)
        supportActionBar?.hide()


        //FireBase Phone Auth

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

         callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

             override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                 // This callback will be invoked in two situations:
                 // 1 - Instant verification. In some cases the phone number can be instantly
                 //     verified without needing to send or enter a verification code.
                 // 2 - Auto-retrieval. On some devices Google Play services can automatically
                 //     detect the incoming verification SMS and perform verification without
                 //     user action.
                 Toast.makeText(this@OTPVerification, "verified", Toast.LENGTH_LONG).show()
                 signInWithPhoneAuthCredential(credential)
             }

             override fun onVerificationFailed(e: FirebaseException) {
                 // This callback is invoked in an invalid request for verification is made,
                 // for instance if the the phone number format is not valid.


                 Toast.makeText(this@OTPVerification, "failed", Toast.LENGTH_LONG).show()
                 if (e is FirebaseAuthInvalidCredentialsException) {
                     // Invalid request
                     errorText.visibility = View.VISIBLE
                 } else if (e is FirebaseTooManyRequestsException) {
                     // The SMS quota for the project has been exceeded'
                     Toast.makeText(this@OTPVerification, "quota exhausted", Toast.LENGTH_LONG).show()
                 }

                 // Show a message and update the UI
             }

             override fun onCodeSent(
                 verificationId: String,
                 token: PhoneAuthProvider.ForceResendingToken
             ) {

                 storedVerificationId = verificationId
                 resendToken = token
                 //dialog box appears
                 Toast.makeText(this@OTPVerification, "OTP sent", Toast.LENGTH_LONG).show()

             }
         }

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                for(snapShot in dataSnapshot.children) {
                    if (snapShot.value.toString() == phoneNumber.text.toString())
                        alreadyExist = true

                    Log.i("mTag", snapShot.value.toString() + "  " + phoneNumber.text.toString())
                }

                startPhoneNumberVerification(phoneNumber.text.toString())

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }


        OTP_BOX.visibility = View.GONE
        verifyOTP.visibility = View.GONE

        //onClickListeners

        signOut.setOnClickListener {
            auth.signOut()
        }


        getOTP.setOnClickListener {
            OTP_BOX.visibility = View.VISIBLE
            getOTP.visibility = View.GONE
            verifyOTP.visibility = View.VISIBLE


//            //check if number already exist in the database
            alreadyExist = false
            database.child("Phone Numbers").addListenerForSingleValueEvent(postListener)

//            Log.i("mTag" , alreadyExist.toString())
        }

        verifyOTP.setOnClickListener {
               verifyPhoneNumberWithCode(storedVerificationId , OTP.text.toString())
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        Toast.makeText(this@OTPVerification, "start phone verification", Toast.LENGTH_LONG).show()

        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result?.user

                    if(alreadyExist)
                    {
                        updateUI(user)
                    }
                    else {
                        val intent = Intent(this, PhoneAuthentication::class.java)
                        intent.putExtra("UserNumber" , phoneNumber.text.toString())
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        errorText.visibility = View.VISIBLE
                        errorText.text = "Sign in failed"
                    }
                    // Update UI
                }
            }
    }
    // [END sign_in_with_phone]

    override fun onStart() {
        super.onStart()



        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if(currentUser != null)
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser? = auth.currentUser) {

        val intent = Intent(this , MainActivity::class.java)
        startActivity(intent)
        finish()

    }


}