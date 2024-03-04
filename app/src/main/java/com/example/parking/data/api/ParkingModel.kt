package com.example.parking.data.api

import com.example.parking.data.db.incidents.Incident
import com.example.parking.data.db.incidents.IncidentsEntity

data class IncidentModel(
    val data: List<Incident>
)

fun List<Incident>.asEntityModel(): List<IncidentsEntity> {
    return this.map {
        IncidentsEntity(
            it.id,
            it.title,
            it.description,
            it.status,
            it.user
        )
    }
}