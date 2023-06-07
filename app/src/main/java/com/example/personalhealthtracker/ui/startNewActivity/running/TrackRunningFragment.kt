package com.example.personalhealthtracker.ui.startNewActivity.running

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentTrackRunningBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class TrackRunningFragment : Fragment() {

    private var _binding: FragmentTrackRunningBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackRunningBinding.inflate(inflater,container,false)
        binding.btnToggleRun.setOnClickListener {

        }

        binding.btnFinishRun.setOnClickListener {
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_trackRunningFragment_to_profileFragment2) }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navigationBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navigationBar?.visibility = View.GONE

    }
    override fun onDestroyView() {
        super.onDestroyView()
        val navigationBar = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        navigationBar?.visibility = View.VISIBLE

        _binding = null
    }


}