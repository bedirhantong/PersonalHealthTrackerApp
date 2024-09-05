package com.example.personalhealthtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.personalhealthtracker.databinding.ActivityMainBinding
import com.example.personalhealthtracker.utils.extension.hide
import com.example.personalhealthtracker.utils.extension.show

class MainActivity : AppCompatActivity() {
    private val navController: NavController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navHostFragment.navController
    }
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        binding.apply {
            bottomNavigationView.background = null
            bottomNavigationView.menu.getItem(1).isEnabled = false

            // Yeni yöntemle tıklama dinleyici ekleme
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.mainScreenFragment -> {
                        navController.navigate(R.id.mainScreenFragment)
                        true
                    }

                    R.id.profileFragment -> {
                        navController.navigate(R.id.profileFragment)
                        true
                    }

                    else -> false
                }
            }

            // FloatingActionButton için tıklama dinleyici ekleme
            fab.setOnClickListener {
                navController.navigate(R.id.startNewActivityFragment)
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.introFragment,
                R.id.mapFragment,
                R.id.signupUserInfo,
                R.id.trackRunningFragment,
                R.id.addExerciseFragment,
                R.id.startNewActivityFragment,
                R.id.loginFragment -> {
                    binding.bottomNavigationView.hide()
                    binding.fab.hide()
                    binding.bottomAppBar.hide()
                }

                else -> {
                    binding.bottomNavigationView.show()
                    binding.fab.show()
                    binding.bottomAppBar.show()
                }
            }
        }
    }
}
