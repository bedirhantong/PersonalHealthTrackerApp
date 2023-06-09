package com.example.personalhealthtracker.data

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.personalhealthtracker.databinding.FragmentAddActivityAndShowToUserBinding

class AddActivityAndShowToUserFragment : Fragment() {

    private var _binding : FragmentAddActivityAndShowToUserBinding?= null
    private val binding get() = _binding!!

    private lateinit var viewModel: HealthyActivitiesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddActivityAndShowToUserBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HealthyActivitiesViewModel::class.java)


        return binding.root
    }











    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}