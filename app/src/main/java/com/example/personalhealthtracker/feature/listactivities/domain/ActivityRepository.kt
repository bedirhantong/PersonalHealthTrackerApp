package com.example.personalhealthtracker.feature.listactivities.domain

import com.example.personalhealthtracker.data.HealthyActivity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    suspend fun getActivities(): Flow<List<HealthyActivity>>
    suspend fun getActivitiesFiltered(order: String): Flow<List<HealthyActivity>>
    suspend fun getActivityById(activityId: String): HealthyActivity?
}