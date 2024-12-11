package com.example.personalhealthtracker.feature.listactivities.presentation

data class WeekDay(
    val dayOfWeek: String,
    val date: String,
    var isSelected: Boolean = false
)