package com.example.personalhealthtracker.data

import java.io.Serializable

data class HealthyActivity(
    val id: String = "",
    val activityName: String,
    val elapsedTime: String,
    val energyConsump: String,
    val kmTravelled: String,
    val imageUrl: String?,
    val polylinePoints: List<SerializableLatLng> = emptyList()
) : Serializable
