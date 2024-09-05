package com.example.personalhealthtracker.feature.startnewactivity.diving

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.personalhealthtracker.databinding.ActivityBreathTakingExerciseBinding
import com.example.personalhealthtracker.databinding.ActivityDivingBinding

lateinit var binding: ActivityDivingBinding

class DivingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_diving)
        com.example.personalhealthtracker.feature.startnewactivity.breathTaking.binding = ActivityBreathTakingExerciseBinding.inflate(layoutInflater)
        val view = com.example.personalhealthtracker.feature.startnewactivity.breathTaking.binding.root
        setContentView(view)



    }
}