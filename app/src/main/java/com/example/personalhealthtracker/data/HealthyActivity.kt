package com.example.personalhealthtracker.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HealthyActivity(
    val id: String = "",
    val activityName: String,
    val kmTravelled: String,
    val energyConsump: String,
    val elapsedTime: String,
    val dateOfAct: Date,
    val polylinePoints: List<SerializableLatLng> = emptyList()
) {
    val dateFormatted: String
        get() = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(dateOfAct)

    val timeFormatted: String
        get() = SimpleDateFormat("HH:mm", Locale.getDefault()).format(dateOfAct)

    val dateTimeFormatted: String
        get() = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(dateOfAct)

    val kcalFormatted: String
        get() = "$energyConsump kcal"

    val kmFormatted: String
        get() = "$kmTravelled km"

    val timeElapsedFormatted: String
        get() = "$elapsedTime min"

}
