package com.example.personalhealthtracker.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.personalhealthtracker.databinding.FragmentSignupChoosingActivitiesBinding


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
//            Navigation.findNavController(view).navigate(R.id.navigateTo_signupChoosingActivities_to_signupResultFragment)
        }

        binding.prevButton.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.navigateTo_signupChoosingActivities_to_signupPhysicalInfo)
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}