package com.example.personalhealthtracker

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
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

        binding.runningView.setOnClickListener {
            binding.runningView.setImageResource(R.drawable.running_icon_selected)
        }
        binding.hikingView.setOnClickListener{
            binding.runningView.setImageResource(R.drawable.hiking_icon_selected)
        }
        binding.cyclingView.setOnClickListener{
            binding.runningView.setImageResource(R.drawable.cycling_icon_selected)
        }
        binding.divingView.setOnClickListener{
            binding.runningView.setImageResource(R.drawable.diving_icon_selected)
        }






        binding.StartButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_startNewActivityFragment2_to_trackRunningFragment)
        }
        binding.cancelButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_startNewActivityFragment2_to_profileFragment2)
            Toast.makeText(requireContext(),"You canceled the starting the new activity",Toast.LENGTH_SHORT).show()

        }


        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}