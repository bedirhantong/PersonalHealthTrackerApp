package com.example.personalhealthtracker.feature.startnewactivity.addexercise

import com.example.personalhealthtracker.data.HealthyActivity
import com.example.personalhealthtracker.feature.listactivities.domain.ActivityRepository

class AddActivityUseCase(private val repository: ActivityRepository) {

    suspend fun saveActivity(activity: HealthyActivity) {
        repository.saveActivity(activity)
    }
}