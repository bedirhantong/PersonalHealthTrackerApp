package com.example.personalhealthtracker.feature.startnewactivity.addexercise

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.data.SerializableLatLng
import com.example.personalhealthtracker.databinding.FragmentAddExerciseBinding
import java.io.Serializable

class AddExerciseFragment : Fragment() {
    private var _binding: FragmentAddExerciseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddExerciseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            uiState.message?.let { showToast(it) }
            if (uiState.navigateToMainScreen) {
                navigateToMainScreen()
            }
        }
    }

    private fun setupUI() {
        val bundle = arguments
        val polylinePoints = bundle?.getSerializable("polylinePoints") as? ArrayList<SerializableLatLng>

        viewModel.setExerciseData(
            activityName = bundle?.getString("activityType", "0") ?: "",
            kmTravelled = bundle?.getString("roadTravelled", "0") ?: "",
            energyConsump = bundle?.getString("caloriesBurned", "0") ?: "",
            elapsedTime = bundle?.getString("timeElapsed", "0") ?: "",
            sourceActivity = bundle?.getString("sourceActivity"),
            polylinePoints = polylinePoints
        )

        val uiState = viewModel.uiState.value
        binding.activityType.text = uiState?.activityName
        binding.kcalView.text = uiState?.energyConsump
        binding.roadTravelledView.text = uiState?.kmTravelled
        binding.durationView.text = uiState?.elapsedTime
        binding.elapsedTimeText.text = if (uiState?.sourceActivity == "Running Activity") "Running" else "Step"
    }

    private fun setupListeners() {
        binding.buttonSave.setOnClickListener {
            viewModel.saveToHistory()
        }
        binding.buttonCancel.setOnClickListener {
            navigateToMainScreen()
        }
    }

    private fun navigateToMainScreen() {
        findNavController().popBackStack(R.id.mainScreenFragment, false)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
