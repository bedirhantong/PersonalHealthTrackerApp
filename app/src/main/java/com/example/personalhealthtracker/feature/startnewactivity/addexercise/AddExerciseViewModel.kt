package com.example.personalhealthtracker.feature.startnewactivity.addexercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalhealthtracker.data.SerializableLatLng
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddExerciseViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()

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

    fun saveToHistory() {
        val userEmail = auth.currentUser?.email ?: ""
        val userId = auth.currentUser?.uid

        val healthyActMap = hashMapOf(
            "energyConsump" to _uiState.value?.energyConsump,
            "kmTravelled" to _uiState.value?.kmTravelled,
            "activityName" to _uiState.value?.activityName,
            "userEmail" to userEmail,
            "dateOfAct" to Timestamp.now(),
            "elapsedTime" to _uiState.value?.elapsedTime,
            "polylinePoints" to _uiState.value?.polylinePoints
        )

        if (userId != null) {
            db.collection("HealthyActivities")
                .add(healthyActMap)
                .addOnSuccessListener { documentReference ->
                    val activityId = documentReference.id
                    db.collection("user")
                        .document(userId)
                        .set(hashMapOf("healthyActivities" to FieldValue.arrayUnion(activityId)), SetOptions.merge())
                        .addOnSuccessListener {
                            _uiState.value = UiState(
                                message = "Saved successfully",
                                navigateToMainScreen = true
                            )
                        }
                        .addOnFailureListener { exception ->
                            _uiState.value = UiState(
                                message = exception.localizedMessage
                            )
                        }
                }
                .addOnFailureListener { exception ->
                    _uiState.value = UiState(
                        message = exception.localizedMessage
                    )
                }
        } else {
            _uiState.value = UiState(
                message = "User not authenticated"
            )
        }
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
