package com.example.personalhealthtracker.ui


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.ActivityMainBinding
import com.example.personalhealthtracker.other.Constants

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        navigateToTrackRunningFragmentIfNeeded(intent)
        supportActionBar?.hide()
    }



    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackRunningFragmentIfNeeded(intent)

    }
    private fun navigateToTrackRunningFragmentIfNeeded(intent: Intent?){
        if (intent?.action == Constants.ACTION_SHOW_TRACK_RUNNING_FRAGMENT){
            findNavController(R.id.fragmentContainerView).navigate(R.id.action_global_trackRunningFragment)

        }
    }

}