package com.example.personalhealthtracker.feature.startnewactivity

import android.animation.ObjectAnimator
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
        val fadeIn = ObjectAnimator.ofFloat(binding.selectedDesc, "alpha", 0f, 1f)
        fadeIn.duration = 500
        fadeIn.start()

        resetBackgroundColors()

        binding.runningView.setOnClickListener {
            changeChosenOne("running", binding.runningView)
            binding.selectedDesc.text = "Running describes a type of gait characterized by an aerial phase in which all feet are above the ground (though there are exceptions)."
        }
        binding.divingView.setOnClickListener {
            changeChosenOne("diving", binding.divingView)
            binding.selectedDesc.text = "Diving is very fun way to find out how deep you can go in the water."
        }
        binding.breathTaking.setOnClickListener {
            changeChosenOne("breathTaking", binding.breathTaking)
            binding.selectedDesc.text = "Test your limits by holding your breath as"
        }
        binding.stepCounting.setOnClickListener {
            changeChosenOne("stepCounting", binding.stepCounting)
            binding.selectedDesc.text = "Step counting is a method of terrestrial locomotion allowing humans and other animals to move rapidly on foot. It is simply defined in athletics terms as a gait in which at regular points during the running cycle both feet are off the ground."
        }

        binding.StartButton.setOnClickListener {
            when (chosenOne) {
                "running" -> {
                    findNavController().navigate(R.id.action_startNewActivityFragment_to_trackRunningFragment)
                }
                else -> {
                    Toast.makeText(requireContext(),"Please choose running activity because that is the " +
                            "only feature exist",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeChosenOne(text: String, view: View) {
        chosenOne = text
        resetBackgroundColors()
        view.setBackgroundColor(
            Color.parseColor("#80000000")
        )
        view.alpha = 1f
        view.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start()
        view.setBackgroundResource(R.drawable.selected_exercise_background)
    }

    private fun resetBackgroundColors() {
        binding.apply {
            runningView.setBackgroundColor(Color.TRANSPARENT)
            runningView.animate().scaleX(1f).scaleY(1f).setDuration(200).start()

            divingView.setBackgroundColor(Color.TRANSPARENT)
            divingView.animate().scaleX(1f).scaleY(1f).setDuration(200).start()

            breathTaking.setBackgroundColor(Color.TRANSPARENT)
            breathTaking.animate().scaleX(1f).scaleY(1f).setDuration(200).start()

            stepCounting.setBackgroundColor(Color.TRANSPARENT)
            stepCounting.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
        }
    }
}
