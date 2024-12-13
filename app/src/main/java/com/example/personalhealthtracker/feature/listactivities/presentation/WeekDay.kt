package com.example.personalhealthtracker.feature.listactivities.presentation

data class WeekDay(
    val dayOfWeek: String,
    val dayOfMonth: Int,
    var isSelected: Boolean = false
)


