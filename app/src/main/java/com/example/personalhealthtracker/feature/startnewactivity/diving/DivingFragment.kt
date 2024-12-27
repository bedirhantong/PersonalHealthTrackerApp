package com.example.personalhealthtracker.feature.startnewactivity.diving

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentDivingBinding

class DivingFragment : Fragment() {

    private lateinit var binding: FragmentDivingBinding

    private var diveTime = 0
    private var maxDepth = 0
    private var isDiving = false
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 1000L

    private var isPaused = false // For pause-resume functionality

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDivingBinding.inflate(inflater, container, false)

        binding.apply {
            // Access views through binding
            btnStart.setOnClickListener { startDive() }
            btnStop.setOnClickListener { stopDive() }
            btnResume.setOnClickListener { resumeDive() }
            btnDone.setOnClickListener { doneDive() }
        }

        return binding.root
    }

    private fun startDive() {
        isDiving = true
        isPaused = false
        diveTime = 0
        maxDepth = 0
        updateUI(0) // Reset UI

        binding.apply {
            btnStart.visibility = View.GONE
            btnStop.visibility = View.VISIBLE
        }

        // Start the diving routine
        handler.post(object : Runnable {
            override fun run() {
                if (isDiving) {
                    diveTime++
                    val newDepth = generateRandomDepth()
                    updateUI(newDepth)

                    val graphX = (diveTime * 20).toFloat()
                    val graphY = (newDepth * 10).toFloat()
                    binding.customGraphView.updateGraph(Pair(graphX, graphY))

                    handler.postDelayed(this, updateInterval)
                }
            }
        })
    }

    private fun stopDive() {
        isDiving = false
        binding.apply {
            btnStop.visibility = View.GONE
            btnResume.visibility = View.VISIBLE
            btnDone.visibility = View.VISIBLE
        }
    }

    private fun resumeDive() {
        isDiving = true
        binding.apply {
            btnResume.visibility = View.GONE
            btnStop.visibility = View.VISIBLE
        }
        handler.post(object : Runnable {
            override fun run() {
                if (isDiving) {
                    diveTime++
                    val newDepth = generateRandomDepth()
                    updateUI(newDepth)

                    val graphX = (diveTime * 20).toFloat()
                    val graphY = (newDepth * 10).toFloat()
                    binding.customGraphView.updateGraph(Pair(graphX, graphY))

                    handler.postDelayed(this, updateInterval)
                }
            }
        })
    }

    private fun doneDive() {
        // Handle the "done" logic, show summary or any relevant action
        isDiving = false
        binding.apply {
            btnDone.visibility = View.GONE
            btnStart.visibility = View.VISIBLE
        }
        // You can show a summary or a message here
    }

    private fun updateUI(newDepth: Int) {
        binding.apply {
            tvDepth.text = getString(R.string.depth_format, newDepth)
            tvTime.text = formatTime(diveTime)

            if (newDepth > maxDepth) {
                maxDepth = newDepth
                tvMaxDepth.text = getString(R.string.max_depth_format, maxDepth)
            }

            pbDepth.progress = newDepth

            val pressure = calculatePressure(newDepth)
            val calories = calculateCalories(newDepth, diveTime)

            tvPressure.text = getString(R.string.pressure_format, pressure)
            tvCalories.text = getString(R.string.calories_format, calories)

            checkForRisk(newDepth, pressure)
        }
    }

    private fun checkForRisk(newDepth: Int, pressure: Float) {
        when {
            newDepth > 20 -> showRiskWarning("High Depth!", 3) // High risk for depth
            pressure > 3f -> showRiskWarning("High Pressure!", 3) // High risk for pressure
            newDepth > 10 -> showRiskWarning("Moderate Depth", 2) // Moderate risk for depth
            else -> showRiskWarning("Safe Dive", 1) // Low risk
        }
    }

    private fun showRiskWarning(message: String, riskLevel: Int) {
        binding.tvRiskWarning.apply {
            when (riskLevel) {
                1 -> {
                    setTextColor(Color.GREEN)
                    text = "Low Risk: $message"
                    animateRiskText(1)
                }
                2 -> {
                    setTextColor(Color.YELLOW)
                    text = "Moderate Risk: $message"
                    animateRiskText(2)
                }
                3 -> {
                    setTextColor(Color.RED)
                    text = "High Risk: $message"
                    animateRiskText(3)
                }
            }
        }
    }

    private fun animateRiskText(riskLevel: Int) {
        val scaleX = ObjectAnimator.ofFloat(binding.tvRiskWarning, "scaleX", 1f, 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(binding.tvRiskWarning, "scaleY", 1f, 1.2f, 1f)
        val alpha = ObjectAnimator.ofFloat(binding.tvRiskWarning, "alpha", 0f, 1f)

        val animatorSet = AnimatorSet().apply {
            playTogether(scaleX, scaleY, alpha)
            duration = 1000
            start()
        }

        if (riskLevel == 3) {
            // Yüksek riskse, daha fazla vurgulama
            val shake = ObjectAnimator.ofFloat(binding.tvRiskWarning, "translationX", -20f, 20f, -20f, 20f, 0f)
            shake.duration = 1500
            shake.start()
        }
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    private fun generateRandomDepth(): Int {
        return (5..30).random()
    }

    private fun calculatePressure(depth: Int): Float {
        return depth / 10f + 1f
    }

    private fun calculateCalories(depth: Int, time: Int): Int {
        return (depth * time / 10)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null) // Stop updates
    }
}
