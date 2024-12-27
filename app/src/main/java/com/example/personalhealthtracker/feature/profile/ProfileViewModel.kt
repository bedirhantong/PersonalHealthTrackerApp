package com.example.personalhealthtracker.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalhealthtracker.data.HealthyActivity
import com.example.personalhealthtracker.feature.listactivities.domain.GetActivitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getActivitiesUseCase: GetActivitiesUseCase
) : ViewModel() {
    private val _activities = MutableStateFlow<List<HealthyActivity>>(emptyList())
    val activities: StateFlow<List<HealthyActivity>> get() = _activities

    private val _userProfile = MutableStateFlow<Map<String, Any>?>(null)
    val userProfile: StateFlow<Map<String, Any>?> get() = _userProfile

    init {
        loadUserProfile()
        loadActivities()
    }

    private fun loadUserProfile() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            FirebaseFirestore.getInstance().collection("users").document(it)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        _userProfile.value = document.data
                    }
                }
                .addOnFailureListener { e ->
                }
        }
    }

    private fun loadActivities() {
        viewModelScope.launch {
            getActivitiesUseCase.execute().collect {
                _activities.value = it
            }
        }
    }
}