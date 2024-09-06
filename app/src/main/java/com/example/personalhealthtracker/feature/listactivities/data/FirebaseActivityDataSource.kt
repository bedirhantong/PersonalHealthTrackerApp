package com.example.personalhealthtracker.feature.listactivities.data

import com.example.personalhealthtracker.data.HealthyActivity
import com.example.personalhealthtracker.data.SerializableLatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.LatLng
import kotlinx.coroutines.tasks.await

class FirebaseActivityDataSource {

    private val db = Firebase.firestore
    private val mAuth = FirebaseAuth.getInstance()

    suspend fun fetchActivities(order: Query.Direction = Query.Direction.DESCENDING): List<HealthyActivity> {
        val snapshot = db.collection("HealthyActivities")
            .whereEqualTo("userEmail", mAuth.currentUser?.email)
            .orderBy("dateOfAct", order)
            .get()
            .await()

        return snapshot.documents.map { document ->
            HealthyActivity(
                activityName = document.getString("activityName") ?: "",
                elapsedTime = document.getString("elapsedTime") ?: "",
                energyConsump = document.getString("energyConsump") ?: "",
                kmTravelled = document.getString("kmTravelled") ?: "",
                imageUrl = document.getString("imageUrl"),
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
}