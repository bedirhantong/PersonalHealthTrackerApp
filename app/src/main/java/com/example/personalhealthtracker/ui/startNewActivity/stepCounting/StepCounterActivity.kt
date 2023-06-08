package com.example.personalhealthtracker.ui.startNewActivity.stepCounting

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.ActivityStepCounterBinding
import com.example.personalhealthtracker.ui.startNewActivity.TrackRunActivity
import com.vmadalin.easypermissions.EasyPermissions


class StepCounterActivity : AppCompatActivity(), SensorEventListener {
    private var sensorManager : SensorManager? = null
    var initialStepCount = -1
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps =0f

    lateinit var binding: ActivityStepCounterBinding
    companion object {
        // Permission
        private const val REQUEST_CODE_ACTIVITY_RECOGNITION = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)
        binding = ActivityStepCounterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var stopTimer:Long = 0
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
            // profile gidilecek
        }

        loadData()
        resetSteps()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

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

//    @SuppressLint("LogNotTimber")
//    @RequiresApi(Build.VERSION_CODES.Q)
//    fun startTracking(){
//        // ACTIVITY_RECOGNITION is not a necessary permission for Android with version below Q.
//        val isActivityRecognitionPermissionFree = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
//        val isActivityRecognitionPermissionGranted = EasyPermissions.hasPermissions(this,
//            ACTIVITY_RECOGNITION)
//        Log.d("TAG", "Is ACTIVITY_RECOGNITION permission granted $isActivityRecognitionPermissionGranted")
//        if (isActivityRecognitionPermissionFree || isActivityRecognitionPermissionGranted) {
//            // Action to be triggered after permission is granted
//            setupStepCounterListener()
//        } else {
//            // Do not have permissions, request them now
//            EasyPermissions.requestPermissions(
//                host = this,
//                rationale = "For showing your step counts and calculate the average pace.",
//                requestCode = REQUEST_CODE_ACTIVITY_RECOGNITION,
//                perms = arrayOf(ACTIVITY_RECOGNITION)
//            )
//        }
//    }
//
//    fun setupStepCounterListener() {
//        // 1
//        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        // 2
//        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
//        // 3
//        stepCounterSensor ?: return
//        // 4
//        sensorManager.registerListener(this@TrackRunActivity, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST)
//    }
//
//    @SuppressLint("LogNotTimber", "SetTextI18n")
//    override fun onSensorChanged(sensorEvent: SensorEvent?) {
//        // 1
//        sensorEvent ?: return
//        // 2
//        sensorEvent.values.firstOrNull()?.let {
//            if (initialStepCount == -1) {
//                initialStepCount = it.toInt()
//            }
//            val currentNumberOfStepCount = it.toInt() - initialStepCount
//            binding.numOfSteps.text = "Number of steps :  $currentNumberOfStepCount"
//        }
//    }
//
//    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
//    }
}