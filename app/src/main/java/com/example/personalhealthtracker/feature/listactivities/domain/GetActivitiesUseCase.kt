package com.example.personalhealthtracker.feature.listactivities.domain

import com.example.personalhealthtracker.data.HealthyActivity
import kotlinx.coroutines.flow.Flow


class GetActivitiesUseCase(private val repository: ActivityRepository) {

    suspend fun execute(): Flow<List<HealthyActivity>> {
        return repository.getActivities()
    }

    suspend fun getFilteredActivities(order: String): Flow<List<HealthyActivity>> {
        return repository.getActivitiesFiltered(order)
    }
}