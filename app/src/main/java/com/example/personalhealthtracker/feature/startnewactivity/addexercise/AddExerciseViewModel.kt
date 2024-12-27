package com.example.personalhealthtracker.feature.startnewactivity.addexercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalhealthtracker.data.HealthyActivity
import com.example.personalhealthtracker.data.SerializableLatLng
import com.example.personalhealthtracker.feature.listactivities.domain.GetActivitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddExerciseViewModel @Inject constructor(
    private val addActivityUseCase: GetActivitiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun setExerciseData(
        activityName: String,
        kmTravelled: String,
        energyConsump: String,
        elapsedTime: String,
        sourceActivity: String?,
        polylinePoints: List<SerializableLatLng>? = null
    ) {
        _uiState.value = _uiState.value.copy(
            activityName = activityName,
            kmTravelled = kmTravelled,
            energyConsump = energyConsump,
            elapsedTime = elapsedTime,
            sourceActivity = sourceActivity,
            polylinePoints = polylinePoints
        )
    }

    fun saveToHistory() {
        viewModelScope.launch {
            try {
                val activity = HealthyActivity(
                    activityName = _uiState.value.activityName,
                    kmTravelled = _uiState.value.kmTravelled,
                    energyConsump = _uiState.value.energyConsump,
                    elapsedTime = _uiState.value.elapsedTime,
                    dateOfAct = Date(),
                    polylinePoints = _uiState.value.polylinePoints ?: emptyList()
                )
                addActivityUseCase.saveActivity(activity)
                _uiState.value = _uiState.value.copy(
                    message = "Saved successfully",
                    navigateToMainScreen = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    message = e.localizedMessage ?: "An error occurred"
                )
            }
        }
    }

    data class UiState(
        val activityName: String = "",
        val kmTravelled: String = "",
        val energyConsump: String = "",
        val elapsedTime: String = "",
        val sourceActivity: String? = null,
        val polylinePoints: List<SerializableLatLng>? = null,
        val message: String? = null,
        val navigateToMainScreen: Boolean = false
    )
}

