package com.example.fcmpushnotification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fcmpushnotification.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {


    private lateinit var binding: ActivityProfileBinding

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handel click logout user
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
        binding.Btnfirebase.setOnClickListener {
            startActivity(Intent(this,FireBaseData::class.java))
        }
    }

    private fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null) {

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{
            val email = firebaseUser.email
            binding.emailTv.text = email
        }

    }
    }
