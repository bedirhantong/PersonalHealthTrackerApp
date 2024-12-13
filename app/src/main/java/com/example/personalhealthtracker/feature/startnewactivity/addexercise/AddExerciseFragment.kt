package com.example.personalhealthtracker.feature.startnewactivity.addexercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.personalhealthtracker.R
import com.example.personalhealthtracker.data.SerializableLatLng
import com.example.personalhealthtracker.databinding.FragmentAddExerciseBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
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
        observeUiState()
    }

    private fun setupUI() {
        val bundle = arguments
        val polylinePoints = bundle?.getSerializable("polylinePoints") as? ArrayList<SerializableLatLng>

        viewModel.setExerciseData(
            activityName = bundle?.getString("activityType", "") ?: "",
            kmTravelled = bundle?.getString("roadTravelled", "0") ?: "",
            energyConsump = bundle?.getString("caloriesBurned", "0") ?: "",
            elapsedTime = bundle?.getString("timeElapsed", "0") ?: "",
            sourceActivity = bundle?.getString("sourceActivity"),
            polylinePoints = polylinePoints
        )
    }

    private fun setupListeners() {
        binding.buttonSave.setOnClickListener {
            viewModel.saveToHistory()
        }
        binding.buttonCancel.setOnClickListener {
            navigateToMainScreen()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { uiState ->
                binding.activityType.text = uiState.activityName
                binding.kcalView.text = uiState.energyConsump
                binding.roadTravelledView.text = uiState.kmTravelled
                binding.durationView.text = uiState.elapsedTime
                binding.elapsedTimeText.text = if (uiState.sourceActivity == "Running Activity") "Running" else "Step"

                uiState.message?.let { showSnackbar(it) }
                if (uiState.navigateToMainScreen) {
                    navigateToMainScreen()
                }
            }
        }
    }

    private fun navigateToMainScreen() {
        findNavController().popBackStack(R.id.mainScreenFragment, false)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

