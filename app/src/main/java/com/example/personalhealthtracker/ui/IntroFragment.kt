package com.example.personalhealthtracker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {

    private var _binding : FragmentIntroBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroBinding.inflate(inflater,container,false)
        val view: View = binding.root

        binding.buttonLogin.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateTo_introFragment5_to_loginFragment)
        }
        binding.buttonSignup.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateTo_introFragment5_to_signupUserInfo)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}