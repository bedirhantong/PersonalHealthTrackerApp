package com.example.personalhealthtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.personalhealthtracker.databinding.FragmentSignupChoosingActivitiesBinding
import com.example.personalhealthtracker.databinding.FragmentSignupPhysicalInfoBinding
import com.example.personalhealthtracker.databinding.FragmentSignupUserInfoBinding

class SignupChoosingActivities : Fragment() {
    private var _binding : FragmentSignupChoosingActivitiesBinding?= null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupChoosingActivitiesBinding.inflate(inflater,container,false)
        val view: View = binding.root


        binding.nextButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateTo_signupChoosingActivities_to_signupPhysicalInfo)
        }

        binding.prevButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.navigateTo_signupChoosingActivities_to_signupResultFragment)
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}