package com.example.personalhealthtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        val navController = findNavController(R.id.fragmentContainerView3)
        bottomNavigationView.setupWithNavController(navController)

    }
}