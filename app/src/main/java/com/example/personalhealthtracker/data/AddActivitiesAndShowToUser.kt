package com.example.personalhealthtracker.data

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.personalhealthtracker.databinding.ActivityAddActivitiesAndShowToUserBinding
import com.example.personalhealthtracker.ui.LoginActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AddActivitiesAndShowToUser : AppCompatActivity() {
    private var _binding: ActivityAddActivitiesAndShowToUserBinding? = null
    private val binding get() = _binding!!

    //Access a Cloud Firestore instance from your Activity
    val db = Firebase.firestore
    // to get user email... to add db
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAddActivitiesAndShowToUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        val sharedPreferences: SharedPreferences = getSharedPreferences("Bilgiler", Context.MODE_PRIVATE) // Initialize sharedPreferences
        binding.activityType.text = sharedPreferences.getString("activityType","0")
        binding.kcalView.text = sharedPreferences.getString("caloriesBurned","0")
        binding.durationView.text = sharedPreferences.getString("timeElapsed","0")
        binding.roadTravelledView.text = sharedPreferences.getString("roadTravelled","0")

        supportActionBar?.hide()

        binding.buttonSave.setOnClickListener {
            saveToHistory()
            startActivity(Intent(this@AddActivitiesAndShowToUser,
                LoginActivity::class.java))
        }
        binding.buttonCancel.setOnClickListener {
            startActivity(Intent(this@AddActivitiesAndShowToUser,
                LoginActivity::class.java))
        }
    }

    private fun saveToHistory(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("Bilgiler", Context.MODE_PRIVATE) // Initialize sharedPreferences
        val activityName = sharedPreferences.getString("activityType","0")!!
        val energyConsump = sharedPreferences.getString("caloriesBurned","0")!!
        val elapsedTime = sharedPreferences.getString("timeElapsed","0")!!
        val kmTravelled = sharedPreferences.getString("roadTravelled","0")!!
        val userEmail = auth.currentUser!!.email!!.toString()
        val dateOfAct = Timestamp.now()


        val healthyActMap = hashMapOf<String,Any>()
        healthyActMap.put("activityName",activityName)
        healthyActMap.put("energyConsump",energyConsump)
        healthyActMap.put("elapsedTime",elapsedTime)
        healthyActMap.put("kmTravelled",kmTravelled)
        healthyActMap.put("userEmail",userEmail)
        healthyActMap.put("dateOfAct",dateOfAct)


        db.collection("HealthyActivities").add(healthyActMap).addOnCompleteListener { task->
            if (task.isSuccessful){
                Toast.makeText(this,"Saved successfully",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}