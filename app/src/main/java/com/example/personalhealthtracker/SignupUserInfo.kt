package com.example.personalhealthtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.personalhealthtracker.databinding.FragmentLoginBinding
import com.example.personalhealthtracker.databinding.FragmentSignupPhysicalInfoBinding
import com.example.personalhealthtracker.databinding.FragmentSignupUserInfoBinding

class SignupUserInfo : Fragment() {

    private var _binding : FragmentSignupUserInfoBinding?= null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupUserInfoBinding.inflate(inflater,container,false)
        val view: View = binding.root

        binding.nextButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateTo_signupUserInfo_to_signupPhysicalInfo)
        }

        binding.prevButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateTo_signupUserInfo_to_introFragment5)

        }

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}