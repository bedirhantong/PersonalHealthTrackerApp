package com.example.personalhealthtracker.feature.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAuth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
            val email = binding.emailViewInLogin.text.toString()
            val password = binding.password1.text.toString()

            if (email == "" || password == "") {
                Toast.makeText(
                    requireContext(), "Lütfen email ve şifre alanlarını boş bırakmayınız!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (isAdded) {
                        if (task.isSuccessful) {
                            // NavOptions ile backstack'i temizleyerek yönlendirme
                            val navOptions = NavOptions.Builder()
                                .setPopUpTo(
                                    R.id.loginFragment,
                                    true
                                )
                                .build()

                            findNavController().navigate(
                                R.id.action_loginFragment_to_mainScreenFragment,
                                null,
                                navOptions
                            )
                        } else {
                            val errorMessage = task.exception?.message
                            Toast.makeText(requireContext(), "$errorMessage", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

            }
        }
        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_signupUserInfo)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}