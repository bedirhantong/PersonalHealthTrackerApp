package com.example.personalhealthtracker.data

import com.example.personalhealthtracker.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ActivityType(val displayName: String, val iconResId: Int) {
    RUNNING("Running", R.drawable.running_icon_selected),
    CYCLING("Cycling", R.drawable.cycling_icon_selected),
    WALKING("Walking", R.drawable.ic_directions_walk_24),
    OTHER("Other", R.drawable.running_icon_selected);

    companion object {
        fun fromString(name: String): ActivityType {
            return entries.find { it.displayName.equals(name, ignoreCase = true) } ?: OTHER
        }
    }
}

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

    val activityIconResId: Int
        get() = ActivityType.fromString(activityName).iconResId

}
