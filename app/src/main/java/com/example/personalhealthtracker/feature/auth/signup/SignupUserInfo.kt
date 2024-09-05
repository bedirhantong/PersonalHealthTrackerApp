package com.example.personalhealthtracker.feature.auth.signup

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.personalhealthtracker.databinding.FragmentSignupUserInfoBinding
import com.google.firebase.auth.FirebaseAuth

class SignupUserInfo : Fragment() {

    private var _binding: FragmentSignupUserInfoBinding? = null
    private val binding get() = _binding!!

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance() // Initialize Firebase Auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupUserInfoBinding.inflate(inflater, container, false)

        binding.nextButton.setOnClickListener {
            val email = binding.emailViewTextInLogin.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(email, password)
            }
        }

        binding.prevButton.setOnClickListener {
            findNavController().navigate(SignupUserInfoDirections.actionSignupUserInfoToIntroFragment())
        }

        return binding.root
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign up success
                    Log.e("SignupUserInfo", "createUserWithEmail:success")
                    findNavController().navigate(SignupUserInfoDirections.actionSignupUserInfoToLoginFragment())
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("SignupUserInfo", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
