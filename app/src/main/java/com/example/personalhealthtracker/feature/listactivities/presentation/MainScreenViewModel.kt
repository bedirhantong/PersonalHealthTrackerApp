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

    private val _filterText = MutableStateFlow("")
    val filterText: StateFlow<String> get() = _filterText

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

    fun applyFilter(order: String) {
        viewModelScope.launch {
            getActivitiesUseCase.getFilteredActivities(order).collect {
                _activities.value = it
                _filterText.value = "Filtered by $order"
            }
        }
    }

    fun resetFilter() {
        _filterText.value = ""
        loadActivities()
    }
}
