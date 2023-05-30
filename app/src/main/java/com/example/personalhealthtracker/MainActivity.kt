package com.example.personalhealthtracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.personalhealthtracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var signupButton: Button
    private lateinit var loginButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        signupButton = binding.buttonSignup
        loginButton = binding.buttonLogin

        signupButton.setOnClickListener {
            openSignupPage()
        }

        loginButton.setOnClickListener {
            openLoginPage()
        }


    }


    fun openSignupPage(){
        //işlem yapabilmek için fragment manager çağırdık
        val fragmentManager = supportFragmentManager
        // fragment managarden fragmentTranscation nesnesi oluşturduk ki işlem yapabilelim
        val fragmentTransaction = fragmentManager.beginTransaction()

        // ilk fragmenti aldık
        val firstFragment = SignupUserInfo()


        fragmentTransaction.replace(com.google.android.material.R.id.container,firstFragment).commit()

    }

    fun openLoginPage(){
        val intent = Intent(this,Login::class.java)
        startActivity(intent)

        // eğer geri dönsün istemezsen...
//        finish()
    }
}