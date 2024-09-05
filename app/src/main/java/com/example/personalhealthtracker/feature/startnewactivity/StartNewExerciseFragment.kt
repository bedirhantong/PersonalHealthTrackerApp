package com.example.personalhealthtracker.feature.startnewactivity

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.databinding.FragmentStartNewExerciseBinding

class StartNewExerciseFragment : Fragment() {
    private var _binding : FragmentStartNewExerciseBinding?= null
    private val binding get() = _binding!!

    private var chosenOne: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartNewExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set default colors for all views
        resetBackgroundColors()

        binding.runningView.setOnClickListener {
            changeChosenOne("running", binding.runningView)
        }
        binding.divingView.setOnClickListener {
            changeChosenOne("diving", binding.divingView)
        }
        binding.breathTaking.setOnClickListener {
            changeChosenOne("breathTaking", binding.breathTaking)
        }
        binding.stepCounting.setOnClickListener {
            changeChosenOne("stepCounting", binding.stepCounting)
        }

        binding.StartButton.setOnClickListener {
            when (chosenOne) {
                "running" -> {
                    findNavController().navigate(R.id.action_startNewActivityFragment_to_trackRunningFragment)
                }
                "stepCounting" -> {

                }
                "breathTaking" -> {
                    Toast.makeText(context,"This Healthy Activity is available now but you cannot save to history for now",Toast.LENGTH_LONG).show()
                }
                "diving" -> {
                    Toast.makeText(context,"This Healthy Activity is not available now",Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(requireContext(),"Please choose running or step counting activity because that is the " +
                            "only feature exist",Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            Toast.makeText(requireContext(),"You canceled the starting the new activity",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeChosenOne(text: String, view: View) {
        chosenOne = text
        resetBackgroundColors()
        view.setBackgroundColor(Color.CYAN)
    }

    private fun resetBackgroundColors() {
        // Reset colors of all exercise views
        binding.runningView.setBackgroundColor(Color.WHITE)
        binding.divingView.setBackgroundColor(Color.WHITE)
        binding.breathTaking.setBackgroundColor(Color.WHITE)
        binding.stepCounting.setBackgroundColor(Color.WHITE)
    }
}
