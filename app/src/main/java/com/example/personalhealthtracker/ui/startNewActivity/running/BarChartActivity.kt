package com.example.personalhealthtracker.ui.startNewActivity.running

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
 import com.example.personalhealthtracker.databinding.ActivityBarChartBinding

class BarChartActivity : AppCompatActivity() {

    private var _binding : ActivityBarChartBinding? = null
    private val  binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBarChartBinding.inflate(layoutInflater)
        setContentView(binding.root )
        binding.apply {




        }




    }


    companion object {
        private val barSetHorizontal = listOf(
            "MON" to 4f,
            "TUE" to 4f,
            "WED" to 4f,
            "THU" to 4f,
            "FRI" to 4f,
            "SAT" to 4f,
            "SUN" to 4f
        )
    }


}