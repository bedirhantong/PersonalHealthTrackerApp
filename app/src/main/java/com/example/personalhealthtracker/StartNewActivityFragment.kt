package com.example.personalhealthtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.personalhealthtracker.databinding.FragmentProfileBinding
import com.example.personalhealthtracker.databinding.FragmentStartNewActivityBinding

class StartNewActivityFragment : Fragment() {


    private var _binding : FragmentStartNewActivityBinding?= null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartNewActivityBinding.inflate(inflater,container,false)
        val view: View = binding.root





        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}