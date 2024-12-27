package com.example.personalhealthtracker.feature.startnewactivity.hiking

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.personalhealthtracker.databinding.FragmentHikingBinding

class HikingFragment : Fragment() {

    private var visible: Boolean = false
    private var statisticsDisplay: View? = null
    private var distanceStat: TextView? = null
    private var elevationStat: TextView? = null
    private var timeElapsedStat: TextView? = null
    private var caloriesStat: TextView? = null
    private var hikingChart: HikingChartView? = null
    private var startButton: Button? = null
    private var timer: Handler? = null
    private var elapsedTime: Long = 0
    private var distance: Float = 0f
    private var elevation: Float = 0f
    private var calories: Float = 0f
    private var isHiking: Boolean = false

    private var _binding: FragmentHikingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHikingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statisticsDisplay = binding.statisticsDisplay
        distanceStat = binding.distanceStat
        elevationStat = binding.elevationStat
        timeElapsedStat = binding.timeElapsedStat
        caloriesStat = binding.caloriesStat
        hikingChart = binding.hikingChart
        startButton = binding.startButton

        startButton?.setOnClickListener {
            if (!isHiking) {
                startHike()
            } else {
                stopHike()
            }
        }
    }

    private fun startHike() {
        isHiking = true
        startButton?.text = "Stop Hike"
        elapsedTime = 0
        distance = 0f
        elevation = 0f
        calories = 0f

        updateStatistics()
        timer = Handler(Looper.getMainLooper())

        // Start updating stats every second
        timer?.postDelayed(object : Runnable {
            override fun run() {
                if (isHiking) {
                    elapsedTime += 1000
                    distance += 0.1f // Simulate distance increase
                    elevation += 1f // Simulate elevation increase
                    calories += 5f // Simulate calories burned

                    // Update UI and Chart
                    updateStatistics()
                    hikingChart?.updateChartData(elevation)
                    timer?.postDelayed(this, 1000)
                }
            }
        }, 1000)
    }

    private fun stopHike() {
        isHiking = false
        startButton?.text = "Start Hike"
        timer?.removeCallbacksAndMessages(null)
    }

    private fun updateStatistics() {
        val timeFormatted = formatTime(elapsedTime)
        distanceStat?.text = "Distance: ${String.format("%.2f", distance)} km"
        elevationStat?.text = "Elevation: ${String.format("%.2f", elevation)} m"
        timeElapsedStat?.text = "Time: $timeFormatted"
        caloriesStat?.text = "Calories: ${String.format("%.0f", calories)} kcal"
    }

    private fun formatTime(milliseconds: Long): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
