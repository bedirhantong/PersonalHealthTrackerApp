package com.example.personalhealthtracker.ui.startNewActivity.stepCounting

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.data.AddActivitiesAndShowToUser
import com.example.personalhealthtracker.databinding.ActivityStepCounterBinding
import com.example.personalhealthtracker.ui.LoginActivity
import com.example.personalhealthtracker.ui.profile.ProfileFragment
import kotlin.math.sqrt


class StepCounterActivity : AppCompatActivity(), SensorEventListener {
    private var sensorManager : SensorManager? = null
    private var running = false
    private var totalSteps = 0
    private var previousTotalSteps =0

    lateinit var binding: ActivityStepCounterBinding
    //yeni
    private var isStepCountingInitialized = false

    private val activityName : String = "Step Counting"
    private var totalDistance : String = ""
    private var step : String = ""
    private var caloriesBurned : String = ""

    private var countedSteps = 0
    private var times = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)
        binding = ActivityStepCounterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var stopTimer:Long = 0
        supportActionBar?.hide()

        // Adım algılama için ivmeölçer sensörünü al
        val accelerometerSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


        // İvmeölçer sensörü kullanılamıyorsa hata mesajı göster
        if (accelerometerSensor == null) {
            Toast.makeText(this, "Accelerometer sensor not available on this device", Toast.LENGTH_SHORT).show()
        } else {
            // İvmeölçer sensörünü dinlemek için kaydet
            sensorManager?.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }







        binding.btnStart.setOnClickListener {
            binding.chronometer.base = SystemClock.elapsedRealtime() + stopTimer
            binding.chronometer.start()

            binding.btnStart.visibility = View.GONE
            binding.btnPause.visibility = View.VISIBLE
            binding.btnDone.visibility = View.VISIBLE
            binding.bottomLayout.setBackgroundColor(getColor(R.color.trackColor))

        }
        binding.btnPause.setOnClickListener {
            stopTimer = binding.chronometer.base- SystemClock.elapsedRealtime()
            binding.chronometer.base = SystemClock.elapsedRealtime() + stopTimer
            binding.chronometer.stop()
            binding.btnStart.visibility = View.VISIBLE
            binding.btnPause.visibility = View.GONE
            binding.btnDone.visibility = View.GONE
            binding.bottomLayout.setBackgroundColor(getColor(R.color.stoptrackColor))
        }

        binding.btnDone.setOnClickListener {
            val sharedPreferences = getSharedPreferences("StepCounter", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("activityType", activityName)
            editor.putString("step", step)
            editor.putString("roadTravelled", totalDistance)
            editor.putString("caloriesBurned", caloriesBurned)
            editor.apply()

            val intent = Intent(this@StepCounterActivity,AddActivitiesAndShowToUser::class.java)
            intent.putExtra("sourceActivity", "StepCounterActivity")
            startActivity(intent)
            this.finish()
        }


        binding.tvStepsTaken.setOnClickListener{
            Toast.makeText(this,"Long tap to reset the steps",Toast.LENGTH_SHORT).show()
        }

        binding.tvStepsTaken.setOnLongClickListener {
            previousTotalSteps = totalSteps
            binding.tvStepsTaken.text = 0.toString()
            saveData()
            true
        }
        loadData()

    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor : Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        if (stepSensor == null){
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        }else{
            sensorManager?.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_NORMAL)
        }
    }


    private fun saveData(){
        val sharedPreferences : SharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("key1",previousTotalSteps)
        editor.apply()
    }

    @SuppressLint("LogNotTimber")
    private fun loadData(){
        val sharedPreferences : SharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber : Int = sharedPreferences.getInt("key1",0)
        Log.d("MainActivity","$savedNumber")
        previousTotalSteps = savedNumber
    }

@SuppressLint("SetTextI18n")
override fun onSensorChanged(event: SensorEvent?) {
    if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val acceleration = sqrt((x * x + y * y + z * z).toDouble())

        if (acceleration > 13) {
            if (!isStepCountingInitialized) {
                isStepCountingInitialized = true
            }

            totalSteps++
            if (totalSteps % 10 == 0) {
                times++
            }
        }

        runOnUiThread {
            val currentSteps = totalSteps + previousTotalSteps
            countedSteps = if (times != 1) {
                (times * 10) + (currentSteps % 10)
            } else {
                currentSteps
            }
            totalDistance = (times*(0.69)).toString()
            caloriesBurned = (times*(0.87)).toString()
            binding.tvStepsTaken.text = times.toString()
            step = times.toString()
            binding.circularProgressBar.setProgressWithAnimation(times.toFloat())
        }
    }
}




    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

}