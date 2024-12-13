package com.example.personalhealthtracker.feature.listactivities.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalhealthtracker.data.HealthyActivity
import com.example.personalhealthtracker.feature.listactivities.domain.GetActivitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getActivitiesUseCase: GetActivitiesUseCase
) : ViewModel() {

    private val _activities = MutableStateFlow<List<HealthyActivity>>(emptyList())
    val activities: StateFlow<List<HealthyActivity>> get() = _activities

    private val _selectedDateActivities = MutableStateFlow<List<HealthyActivity>>(emptyList())
    val selectedDateActivities: StateFlow<List<HealthyActivity>> get() = _selectedDateActivities

    init {
        loadActivities()
    }

    private fun loadActivities() {
        // Fetch all activities initially
        viewModelScope.launch {
            getActivitiesUseCase.execute().collect {
                _activities.value = it
            }
        }
    }

    fun filterActivitiesByDate(selectedDate: Date) {
        viewModelScope.launch {
            val calendar = Calendar.getInstance().apply {
                time = selectedDate
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val startOfDay = calendar.time
            calendar.add(Calendar.DATE, 1)
            val endOfDay = calendar.time
            getActivitiesUseCase.getActivitiesFilteredByDate(startOfDay, endOfDay).collect {
                _selectedDateActivities.value = it
            }
        }
    }
}

