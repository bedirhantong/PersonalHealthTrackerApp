package com.example.personalhealthtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}