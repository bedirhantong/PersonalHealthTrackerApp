package com.example.personalhealthtracker.feature.listactivities.data

import com.example.personalhealthtracker.data.HealthyActivity
import com.example.personalhealthtracker.feature.listactivities.domain.ActivityRepository
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ActivityRepositoryImpl(
    private val dataSource: FirebaseActivityDataSource
) : ActivityRepository {

    override suspend fun getActivities(): Flow<List<HealthyActivity>> = flow {
        val activities = dataSource.fetchActivities(Query.Direction.DESCENDING)
        emit(activities)
    }

    override suspend fun getActivitiesFiltered(order: String): Flow<List<HealthyActivity>> = flow {
        val direction = if (order == "ASCENDING") Query.Direction.ASCENDING else Query.Direction.DESCENDING
        val activities = dataSource.fetchActivities(direction)
        emit(activities)
    }

    override suspend fun getActivityById(activityId: String): HealthyActivity? {
        return dataSource.fetchActivityById(activityId)
    }
}