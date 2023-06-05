package com.example.personalhealthtracker.ui.startNewActivity

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.ActivityStepCounterBinding

class StepCounterActivity : AppCompatActivity(), SensorEventListener {
    private var sensorManager : SensorManager? = null

    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps =0f

    lateinit var binding: ActivityStepCounterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)
        binding = ActivityStepCounterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor : Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null){
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        }else{
            sensorManager?.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (running){

            if (p0 != null) {
                totalSteps = p0.values[0]
            }
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            binding.tvStepsTaken.text = ("$currentSteps")
            binding.circularProgressBar.apply {
                setProgressWithAnimation(currentSteps.toFloat())


            }
        }
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }





}