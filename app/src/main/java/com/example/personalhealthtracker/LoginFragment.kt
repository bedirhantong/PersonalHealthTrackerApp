package com.example.personalhealthtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.personalhealthtracker.databinding.FragmentIntroBinding
import com.example.personalhealthtracker.databinding.FragmentLoginBinding
import com.example.personalhealthtracker.databinding.FragmentSignupUserInfoBinding

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding?= null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        val view: View = binding.root


        binding.loginButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateTo_FromLogin_ToProfile)
        }

        binding.signupButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateTo_FromLogin_ToSignup)

        }

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}