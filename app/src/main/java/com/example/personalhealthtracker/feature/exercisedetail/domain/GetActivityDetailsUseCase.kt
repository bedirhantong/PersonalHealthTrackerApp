package com.example.personalhealthtracker.feature.exercisedetail.domain

import com.example.personalhealthtracker.data.HealthyActivity
import com.example.personalhealthtracker.feature.listactivities.domain.ActivityRepository
import javax.inject.Inject

class GetActivityDetailsUseCase @Inject constructor(
    private val repository: ActivityRepository
) {
    suspend fun execute(activityId: String): HealthyActivity? {
        return repository.getActivityById(activityId)
    }
}