package com.example.personalhealthtracker.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.ActivityAddActivitiesAndShowToUserBinding
import com.example.personalhealthtracker.ui.LoginActivity

class AddActivitiesAndShowToUser : AppCompatActivity() {
    private var _binding: ActivityAddActivitiesAndShowToUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HealthyActivitiesViewModel


    @SuppressLint("CommitTransaction")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAddActivitiesAndShowToUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(HealthyActivitiesViewModel::class.java)

        val sharedPreferences: SharedPreferences = getSharedPreferences("Bilgiler", Context.MODE_PRIVATE) // Initialize sharedPreferences
        var message = ""
        viewModel.result.observe(this, Observer  {
            message = if (it == null){
                getString(R.string.activity_added)
            }else{
                getString(R.string.error, it.message)
            }

            Toast.makeText(this@AddActivitiesAndShowToUser,
            message,Toast.LENGTH_SHORT).show()

        })
        Toast.makeText(this@AddActivitiesAndShowToUser,
            message,Toast.LENGTH_SHORT).show()

        binding.activityType.text = sharedPreferences.getString("activityType","0")
        binding.kcalView.text = sharedPreferences.getString("caloriesBurned","0")
        binding.durationView.text = sharedPreferences.getString("timeElapsed","0")
        binding.roadTravelledView.text = sharedPreferences.getString("roadTravelled","0")

        binding.buttonSave.setOnClickListener {
            val healthyActivity = HealthyActivity()

            healthyActivity.activityName = sharedPreferences.getString("activityType","0")
            healthyActivity.roadTravelled = sharedPreferences.getString("roadTravelled","0")
            healthyActivity.timeElapsed = sharedPreferences.getString("timeElapsed","0")
            healthyActivity.energyCons = sharedPreferences.getString("caloriesBurned","0")


            viewModel.addActivity(healthyActivity)

            startActivity(Intent(this@AddActivitiesAndShowToUser,
                LoginActivity::class.java))
        }
        binding.buttonCancel.setOnClickListener {
            startActivity(Intent(this@AddActivitiesAndShowToUser,
                LoginActivity::class.java))

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}