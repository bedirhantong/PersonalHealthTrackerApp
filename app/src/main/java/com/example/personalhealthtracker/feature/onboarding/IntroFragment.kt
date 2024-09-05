package com.example.personalhealthtracker.feature.onboarding

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogin.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_introFragment_to_loginFragment)
        }
        binding.buttonSignup.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_introFragment_to_signupUserInfo)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}