package com.example.parking.data.api

import com.example.parking.data.db.incidents.Incident
import com.example.parking.data.db.incidents.IncidentsEntity

data class IncidentModel(
    val data: List<Incident>
)

