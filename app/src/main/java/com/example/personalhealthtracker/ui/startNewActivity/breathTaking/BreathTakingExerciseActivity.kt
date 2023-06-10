package com.example.personalhealthtracker.ui.startNewActivity.breathTaking

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.example.personalhealthtracker.databinding.ActivityBreathTakingExerciseBinding
import com.example.personalhealthtracker.ui.LoginActivity


@SuppressLint("StaticFieldLeak")
lateinit var binding: ActivityBreathTakingExerciseBinding

class BreathTakingExerciseActivity : AppCompatActivity() {
    private val actType = "Breath Taking Activity"
    private var actDuration = "30"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_breath_taking_exercise)
        binding = ActivityBreathTakingExerciseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonStartInBreathTaking.setOnClickListener {
            startBreathing()
            binding.buttonStartInBreathTaking.visibility = View.GONE
            binding.buttonStopInBreathTaking.visibility = View.VISIBLE
        }
        binding.buttonStopInBreathTaking.setOnClickListener {
            binding.buttonStartInBreathTaking.visibility = View.VISIBLE
            binding.buttonStopInBreathTaking.visibility = View.GONE
        }

    }

    private fun startBreathing(){
        var count = 30
        var time  = 30000

        object : CountDownTimer(time.toLong(),1000){
            @SuppressLint("SetTextI18n")
            override fun onTick(millisTillFinished: Long) {
                if (count >20){
                    binding.headerOfBreathTaking.text = "TAKE : ${(millisTillFinished-20000)/1000}"
                    count--
                }else if (count > 10){
                    binding.headerOfBreathTaking.text = "HOLD : ${(millisTillFinished-10000)/1000}"
                    count--
                }else if (count > 0 ){
                    binding.headerOfBreathTaking.text = "RELEASE : ${millisTillFinished/1000}"
                    count--
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                binding.headerOfBreathTaking.text = "YOU DID A GREAT JOB!"
                binding.buttonStartInBreathTaking.visibility = View.GONE
                binding.buttonStopInBreathTaking.visibility = View.GONE
                binding.buttonFinish.visibility = View.VISIBLE

                binding.buttonFinish.setOnClickListener {




                    startActivity(Intent(this@BreathTakingExerciseActivity, LoginActivity::class.java))
                }
            }
        }.start()



    }

}