package com.example.personalhealthtracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.ActivityLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        val navController = findNavController(R.id.fragmentContainerView2)
        bottomNavigationView.setupWithNavController(navController)


    }






}





