package com.example.personalhealthtracker.feature.listactivities.domain

import com.example.personalhealthtracker.data.HealthyActivity
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ActivityRepository {
    suspend fun getActivities(): Flow<List<HealthyActivity>>
    suspend fun getActivitiesFiltered(order: String): Flow<List<HealthyActivity>>
    suspend fun getActivityById(activityId: String): HealthyActivity?
    suspend fun saveActivity(activity: HealthyActivity)
    suspend fun getActivitiesFilteredByDate(startDate: Date, endDate: Date) : Flow<List<HealthyActivity>>
}