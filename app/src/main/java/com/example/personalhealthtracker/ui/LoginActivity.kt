package com.example.personalhealthtracker.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.ActivityLoginBinding
import com.example.personalhealthtracker.other.Constants.ACTION_SHOW_TRACK_RUNNING_FRAGMENT
import com.google.android.material.bottomnavigation.BottomNavigationView


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        navigateToTrackRunningFragmentIfNeeded(intent)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        val navController = findNavController(R.id.fragmentContainerView2)
        bottomNavigationView.setupWithNavController(navController)


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackRunningFragmentIfNeeded(intent)

    }

    private fun navigateToTrackRunningFragmentIfNeeded(intent: Intent?){
        if (intent?.action == ACTION_SHOW_TRACK_RUNNING_FRAGMENT){
            findNavController(R.id.fragmentContainerView).navigate(R.id.action_global_trackRunningFragment)

        }
    }





}





