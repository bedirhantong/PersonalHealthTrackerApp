package com.example.personalhealthtracker.ui.startNewActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.personalhealthtracker.databinding.ActivityAddActivitiesAndShowToUserBinding

class AddActivitiesAndShowToUser : AppCompatActivity() {
    private var _binding: ActivityAddActivitiesAndShowToUserBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_activities_and_show_to_user)

        _binding = ActivityAddActivitiesAndShowToUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}