package com.example.personalhealthtracker.ui.startNewActivity.running
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.personalhealthtracker.databinding.ActivityLineChartBinding


class LineChartActivity : AppCompatActivity() {
    private var _binding : ActivityLineChartBinding? = null
    private val  binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLineChartBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }



}