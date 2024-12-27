package com.example.personalhealthtracker.core

import com.example.personalhealthtracker.feature.listactivities.data.ActivityRepositoryImpl
import com.example.personalhealthtracker.feature.listactivities.data.FirebaseActivityDataSource
import com.example.personalhealthtracker.feature.listactivities.domain.ActivityRepository
import com.example.personalhealthtracker.feature.listactivities.domain.GetActivitiesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideFirebaseActivityDataSource(): FirebaseActivityDataSource = FirebaseActivityDataSource()

    @Provides
    fun provideActivityRepository(
        dataSource: FirebaseActivityDataSource
    ): ActivityRepository = ActivityRepositoryImpl(dataSource)

    @Provides
    fun provideGetActivitiesUseCase(repository: ActivityRepository): GetActivitiesUseCase {
        return GetActivitiesUseCase(repository)
    }
}
