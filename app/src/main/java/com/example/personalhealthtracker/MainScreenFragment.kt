package com.example.personalhealthtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.personalhealthtracker.databinding.FragmentMainScreenBinding
import com.example.personalhealthtracker.databinding.FragmentProfileBinding

class MainScreenFragment : Fragment() {
    private var _binding : FragmentMainScreenBinding?= null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater,container,false)
        val view: View = binding.root

        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}