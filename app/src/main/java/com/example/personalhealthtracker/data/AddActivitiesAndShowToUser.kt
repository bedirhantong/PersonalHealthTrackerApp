package com.example.personalhealthtracker.data

import android.annotation.SuppressLint
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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


private lateinit var sharedPreferences : SharedPreferences
private lateinit var sourceActivity: String


private lateinit var activityName: String
private lateinit var kmTravelled: String
private lateinit var energyConsump: String
private lateinit var userEmail: String
private lateinit var dateOfAct: Timestamp
private lateinit var elapsedTime: String
class AddActivitiesAndShowToUser : AppCompatActivity() {
    private var _binding: ActivityAddActivitiesAndShowToUserBinding? = null
    private val binding get() = _binding!!

    //Access a Cloud Firestore instance from your Activity
    val db = Firebase.firestore
    // to get user email... to add db
    private lateinit var auth : FirebaseAuth


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAddActivitiesAndShowToUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        sourceActivity = intent.getStringExtra("sourceActivity").toString()

        if (sourceActivity == "Running Activity"){
            sharedPreferences = getSharedPreferences("Bilgiler", Context.MODE_PRIVATE) // Initialize sharedPreferences
            binding.activityType.text = sharedPreferences.getString("activityType","0")
            binding.kcalView.text = sharedPreferences.getString("caloriesBurned","0")
            binding.roadTravelledView.text = sharedPreferences.getString("roadTravelled","0")
            binding.durationView.text = sharedPreferences.getString("timeElapsed","0")

        }else{
            sharedPreferences = getSharedPreferences("StepCounter", Context.MODE_PRIVATE) // Initialize sharedPreferences
            binding.activityType.text = sharedPreferences.getString("activityType","0")
            binding.kcalView.text = sharedPreferences.getString("caloriesBurned","0")
            binding.roadTravelledView.text = sharedPreferences.getString("totalDistance","0")
            binding.durationView.text = sharedPreferences.getString("step","0")
            binding.elapsedTimeText.text = "Step"
        }


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
        sourceActivity = intent.getStringExtra("sourceActivity").toString()
        val healthyActMap = hashMapOf<String,Any>()
        if (sourceActivity == "Running Activity"){
            sharedPreferences = getSharedPreferences("Bilgiler", Context.MODE_PRIVATE) // Initialize sharedPreferences
            elapsedTime = sharedPreferences.getString("timeElapsed","0")!!
            activityName = sharedPreferences.getString("activityType","0")!!
            kmTravelled = sharedPreferences.getString("roadTravelled","0")!!
            energyConsump = sharedPreferences.getString("caloriesBurned","0")!!
        }
        else{
            sharedPreferences = getSharedPreferences("StepCounter", Context.MODE_PRIVATE) // Initialize sharedPreferences
            elapsedTime = sharedPreferences.getString("step","0")!!
            activityName = sharedPreferences.getString("activityType","0")!!
            kmTravelled = sharedPreferences.getString("roadTravelled","0")!!
            energyConsump = sharedPreferences.getString("caloriesBurned","0")!!
        }

        userEmail = auth.currentUser!!.email!!.toString()
        dateOfAct = Timestamp.now()

        healthyActMap.put("energyConsump",energyConsump)
        healthyActMap.put("kmTravelled",kmTravelled)
        healthyActMap.put("activityName",activityName)
        healthyActMap.put("userEmail",userEmail)
        healthyActMap.put("dateOfAct",dateOfAct)
        healthyActMap.put("elapsedTime",elapsedTime)


        val userId = FirebaseAuth.getInstance().currentUser?.uid



        // Aktiviteyi "HealthyActivities" koleksiyonuna ekle
        db.collection("HealthyActivities")
            .add(healthyActMap)
            .addOnSuccessListener { documentReference ->
                val activityId = documentReference.id

                // Kullanıcının "user" belgesini güncelle
                if (userId != null) {
                    db.collection("user")
                        .document(userId)
                        .update("healthyActivities", FieldValue.arrayUnion(activityId))
                        .addOnSuccessListener {
                            Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }




















//
//
//
//        db.collection("HealthyActivities").add(healthyActMap).addOnCompleteListener { task->
//            if (task.isSuccessful){
//
//                Toast.makeText(this,"Saved successfully",Toast.LENGTH_SHORT).show()
//            }
//        }.addOnFailureListener { exception ->
//            Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
//        }

    }





    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}