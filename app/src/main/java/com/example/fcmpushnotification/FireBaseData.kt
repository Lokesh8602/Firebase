package com.example.fcmpushnotification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_fire_base_data.*

class FireBaseData : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_base_data)
        saveButton.setOnClickListener{
            val firstName = inputFirstName.text.toString()
            val lastName = inputLastName.text.toString()

            saveFireStore(firstName,lastName)
        }
        readFireStoreData()
    }
    fun saveFireStore(firstname: String, lastname: String){
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["firstName"] = firstname
        user["lastName"] = lastname

        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(this@FireBaseData, "record added Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this@FireBaseData, "record Failed to add", Toast.LENGTH_SHORT).show()
            }
        readFireStoreData()


    }
    // Reterive data
    fun readFireStoreData(){
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .get()
            .addOnCompleteListener {
                val result: StringBuffer = StringBuffer()
                if (it.isSuccessful){
                    for (document in it.result!!){
                        result.append(document.data.getValue("firstName")).append("")
                            .append(document.data.getValue("lastName")).append("\n\n")
                    }
                    textviewResult.setText(result)

                }

            }

    }
}