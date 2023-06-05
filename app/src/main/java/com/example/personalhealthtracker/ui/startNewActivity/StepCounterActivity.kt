package com.example.personalhealthtracker.ui.startNewActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    private fun resetSteps(){
        binding.tvStepsTaken.setOnClickListener{
            Toast.makeText(this,"Long tap to reset the steps",Toast.LENGTH_SHORT).show()
        }

        binding.tvStepsTaken.setOnLongClickListener {
            previousTotalSteps = totalSteps
            binding.tvStepsTaken.text = 0.toString()
            saveData()

            true
        }
    }

    private fun saveData(){
        val sharedPreferences : SharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putFloat("key1",previousTotalSteps)
        editor.apply()
    }

    @SuppressLint("LogNotTimber")
    private fun loadData(){
        val sharedPreferences : SharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber : Float = sharedPreferences.getFloat("key1",0f)

        Log.d("MainActivity","$savedNumber")
        previousTotalSteps = savedNumber

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