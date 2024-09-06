package com.example.personalhealthtracker.data
import java.io.Serializable


data class SerializableLatLng(
    val latitude: Double,
    val longitude: Double
) : Serializable
