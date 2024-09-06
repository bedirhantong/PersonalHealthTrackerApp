package com.example.personalhealthtracker.feature.exercisedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalhealthtracker.data.HealthyActivity
import com.example.personalhealthtracker.data.SerializableLatLng
import com.example.personalhealthtracker.feature.exercisedetail.domain.GetActivityDetailsUseCase
import com.example.personalhealthtracker.feature.startnewactivity.addexercise.AddExerciseViewModel.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseDetailViewModel @Inject constructor(
    private val getActivityDetailsUseCase: GetActivityDetailsUseCase
) : ViewModel() {


    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState


    fun setExerciseData(
        activityName: String,
        kmTravelled: String,
        energyConsump: String,
        elapsedTime: String,
        sourceActivity: String?,
        polylinePoints: ArrayList<SerializableLatLng>? = null // Yeni parametre
    ) {
        _uiState.value = UiState(
            activityName = activityName,
            kmTravelled = kmTravelled,
            energyConsump = energyConsump,
            elapsedTime = elapsedTime,
            sourceActivity = sourceActivity,
            polylinePoints = polylinePoints
        )
    }

    data class UiState(
        val activityName: String = "",
        val kmTravelled: String = "",
        val energyConsump: String = "",
        val elapsedTime: String = "",
        val sourceActivity: String? = null,
        val polylinePoints: ArrayList<SerializableLatLng>? = null, // Yeni property
        val message: String? = null,
        val navigateToMainScreen: Boolean = false
    )
}
