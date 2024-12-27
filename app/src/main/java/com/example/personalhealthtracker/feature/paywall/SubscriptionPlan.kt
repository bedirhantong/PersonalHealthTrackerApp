package com.example.personalhealthtracker.feature.paywall

data class SubscriptionPlan(
    val title: String,
    val price: String,
    val features: String,
    var isSelected: Boolean = false
)
