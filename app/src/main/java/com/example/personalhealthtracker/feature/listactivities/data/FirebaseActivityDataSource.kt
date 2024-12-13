package com.example.personalhealthtracker.feature.listactivities.data

import com.example.personalhealthtracker.data.HealthyActivity
import com.example.personalhealthtracker.data.SerializableLatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Date

class FirebaseActivityDataSource {

    private val db = Firebase.firestore
    private val mAuth = FirebaseAuth.getInstance()

    suspend fun saveActivity(activity: HealthyActivity) {
        val userId = mAuth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

        val activityMap = hashMapOf(
            "activityName" to activity.activityName,
            "kmTravelled" to activity.kmTravelled,
            "energyConsump" to activity.energyConsump,
            "elapsedTime" to activity.elapsedTime,
            "dateOfAct" to activity.dateOfAct,
            "polylinePoints" to activity.polylinePoints,
            "userId" to userId
        )

        db.collection("HealthyActivities")
            .add(activityMap)
            .await()
    }

    suspend fun fetchActivities(order: Query.Direction = Query.Direction.DESCENDING): List<HealthyActivity> {
        val userId = mAuth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

        val snapshot = db.collection("HealthyActivities")
            .whereEqualTo("userId", userId)
            .orderBy("dateOfAct", order)
            .get()
            .await()

        return snapshot.documents.map { document ->
            HealthyActivity(
                activityName = document.getString("activityName") ?: "",
                elapsedTime = document.getString("elapsedTime") ?: "",
                energyConsump = document.getString("energyConsump") ?: "",
                kmTravelled = document.getString("kmTravelled") ?: "",
                dateOfAct = document.getDate("dateOfAct") ?: throw IllegalStateException("Date not found"),
                polylinePoints = document.get("polylinePoints") as List<SerializableLatLng>? ?: emptyList()
            )
        }
    }

    suspend fun fetchActivityById(id: String): HealthyActivity? {
        return try {
            val document = db.collection("HealthyActivities").document(id).get().await()
            document.toObject(HealthyActivity::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun fetchActivitiesByDate(
        startDate: Date,
        endDate: Date,
        order: Query.Direction = Query.Direction.DESCENDING
    ): List<HealthyActivity> {
        val userId = mAuth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

        val snapshot = db.collection("HealthyActivities")
            .whereEqualTo("userId", userId)
            .whereGreaterThanOrEqualTo("dateOfAct", startDate)
            .whereLessThanOrEqualTo("dateOfAct", endDate)
            .orderBy("dateOfAct", order)
            .get()
            .await()

        return snapshot.documents.map { document ->
            HealthyActivity(
                activityName = document.getString("activityName") ?: "",
                elapsedTime = document.getString("elapsedTime") ?: "",
                energyConsump = document.getString("energyConsump") ?: "",
                kmTravelled = document.getString("kmTravelled") ?: "",
                dateOfAct = document.getDate("dateOfAct") ?: throw IllegalStateException("Date not found"),
                polylinePoints = document.get("polylinePoints") as List<SerializableLatLng>? ?: emptyList()
            )
        }
    }


}

