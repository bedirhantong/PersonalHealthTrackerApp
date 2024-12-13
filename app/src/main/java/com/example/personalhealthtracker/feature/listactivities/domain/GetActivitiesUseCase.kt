package com.example.personalhealthtracker.feature.listactivities.domain

import com.example.personalhealthtracker.data.HealthyActivity
import kotlinx.coroutines.flow.Flow
import java.util.Date


class GetActivitiesUseCase(private val repository: ActivityRepository) {

    suspend fun execute(): Flow<List<HealthyActivity>> {
        return repository.getActivities()
    }
    suspend fun saveActivity(activity: HealthyActivity) {
        repository.saveActivity(activity)
    }

    suspend fun getFilteredActivities(order: String): Flow<List<HealthyActivity>> {
        return repository.getActivitiesFiltered(order)
    }

    suspend fun getFilteredActivitiesByDate(startDate: Date, endDate: Date): Flow<List<HealthyActivity>> {
        return repository.getActivitiesFilteredByDate(startDate, endDate)
    }
}