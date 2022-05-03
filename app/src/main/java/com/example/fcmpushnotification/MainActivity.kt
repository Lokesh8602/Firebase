package com.example.fcmpushnotification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.fcmpushnotification.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    //constants
    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure the Google SighIn
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken( getString(R.string.default_web_client_id))
            .requestEmail() // we only need email fromg google account
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //Google SignIn Button, Click to begin Google SignIn
        binding.googleSignInBtn.setOnClickListener {
            //begin Google SignIn
            Log.d(TAG,"on create: begin Google SignIn")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)

        }

    }
    private fun checkUser(){
        //check if user is logged in or not
        val firebaseuser = firebaseAuth.currentUser
        if (firebaseuser != null){
            //start profile Activity
            startActivity(Intent(this@MainActivity,ProfileActivity::class.java))
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN){
            Log.d(TAG,"onActivityResult: Google SignIn intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Google SignIn Success, now auth with firebase
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)


            }
            catch (e: Exception){
                //Failed Google  SignIn
                Log.d(TAG,"onActivityResult:${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG,"firebaseAuthWithGoogleAccount: begin firebase auth with google account")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                Log.d(TAG,"firebaseAuthWithGoogleAccount: LoggedIn")

                //get loggedIn user
                val firebaseUser = firebaseAuth.currentUser
                //get user info
                val uid = firebaseUser!!.uid
                val email = firebaseUser!!.email

                Log.d(TAG,"firebaseAuthWithGoogleAccount: Uid: $uid")
                Log.d(TAG,"firebaseAuthWithGoogleAccount: Email: $email")

                //check if user is new or exisiting
                if (authResult.additionalUserInfo!!.isNewUser){
                    // user is new - Account created
                    Log.d(TAG,"firebaseAuthWithGoogleAccount: Account created...\n$email")
                    Toast.makeText(this@MainActivity,"Account created...\n$email", Toast.LENGTH_SHORT).show()
                    /* startActivity(Intent(this@MainActivity,ProfileActivity::class.java))
                     *//*finish()*/

                }
                else
                {
                    Log.d(TAG,"firebaseAuthWithGoogleAccount:Existing user....\n$email")
                    Toast.makeText(this@MainActivity,"LoggedIn...\n$email", Toast.LENGTH_SHORT).show()

                }
                //start profile Activity
                startActivity(Intent(this@MainActivity,ProfileActivity::class.java))
                finish()



            }
            .addOnFailureListener{ e ->
                //login failed
                Log.d(TAG,"firebaseAuthWithGoogleAccount: Loggin Failed due to ${e.message}")
                Toast.makeText(this@MainActivity,"Loggin Failed due to  ${e.message} ", Toast.LENGTH_SHORT).show()

            }

        val crash = findViewById<Button>(R.id.crash)
        crash.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

    }


    }
