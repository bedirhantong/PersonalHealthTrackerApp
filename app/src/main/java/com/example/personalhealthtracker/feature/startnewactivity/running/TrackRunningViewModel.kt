package com.example.personalhealthtracker.feature.startnewactivity.running

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import java.text.DecimalFormat
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class TrackRunningViewModel : ViewModel() {

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> get() = _uiState

    private var prevLocation: LatLng? = null
    private var totalDistance = 0.0
    private var totalEnergyConsumption = 0.0
    private var totalSteps = 0
    private var stopTimer: Long = 0
    private var averageSpeed = 0.0
    var elapsedSecond = 0
    var formattedCalories = "0"
    var formattedDistance = ""

    var currentLocation: LatLng? = null
    var polyline: Polyline? = null
    val polylinePoints = mutableListOf<LatLng>()
    val polylineOptions = PolylineOptions().color(android.graphics.Color.BLUE).width(9f)

    fun startTimer() {
        // Start the timer logic and notify UI state
    }

    fun stopTimer() {
        // Stop the timer logic and notify UI state
    }

    fun updateLocation(location: LatLng) {
        currentLocation = location
        if (prevLocation != null) {
            val dist = calculateDistance()
            totalDistance += dist
            totalSteps += (totalDistance / 1000).toInt()
            formattedDistance = DecimalFormat("#.###").format(totalDistance)
            elapsedSecond = ((SystemClock.elapsedRealtime() - stopTimer) / 1000).toInt()
            totalEnergyConsumption += calculateCaloriesBurned(totalSteps, elapsedSecond, 25)
            formattedCalories = DecimalFormat("#.###").format(totalEnergyConsumption)
            averageSpeed = calculateAverageSpeed(totalSteps, elapsedSecond)
        }

        polylinePoints.add(prevLocation!!)
        _uiState.value = UiState(formattedDistance, formattedCalories, averageSpeed.toString(), totalSteps)
        prevLocation = location
    }

    fun setInitialLocation(location: LatLng) {
        prevLocation = location
        currentLocation = location
    }

    private fun calculateDistance(): Double {
        val R = 6371000 // Radius of the earth in meters
        val dLat = Math.toRadians(currentLocation!!.latitude - prevLocation!!.latitude)
        val dLng = Math.toRadians(currentLocation!!.longitude - prevLocation!!.longitude)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(prevLocation!!.latitude)) * cos(Math.toRadians(currentLocation!!.latitude)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

    private fun calculateCaloriesBurned(steps: Int, time: Int, weight: Int): Double {
        val MET = 0.029 * weight
        return MET * (time / 60.0) * 60 // calories per minute
    }

    private fun calculateAverageSpeed(steps: Int, time: Int): Double {
        return if (time > 0) (steps / time.toDouble()) * 3600 else 0.0
    }

    data class UiState(
        val formattedDistance: String = "",
        val formattedCalories: String = "",
        val averageSpeed: String = "",
        val totalSteps: Int = 0
    )
}
