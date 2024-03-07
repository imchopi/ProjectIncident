package com.example.parking.ui.incident.add


data class AddIncidentUiState(
    val id: Int? = 0,
    val title: String = "",
    val description: String = "",
    val status: String = "",
    val user: Int = 0
)
