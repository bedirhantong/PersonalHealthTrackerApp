package com.example.personalhealthtracker.feature.listactivities.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalhealthtracker.data.HealthyActivity
import com.example.personalhealthtracker.feature.listactivities.domain.GetActivitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getActivitiesUseCase: GetActivitiesUseCase
) : ViewModel() {

    private val _activities = MutableStateFlow<List<HealthyActivity>>(emptyList())
    val activities: StateFlow<List<HealthyActivity>> get() = _activities

    init {
        loadActivities()
    }

    private fun loadActivities() {
        viewModelScope.launch {
            getActivitiesUseCase.execute().collect {
                _activities.value = it
            }
        }
    }

    fun getActivities() {
        viewModelScope.launch {
            getActivitiesUseCase.execute().collect {
                _activities.value = it
            }
        }
    }

}

