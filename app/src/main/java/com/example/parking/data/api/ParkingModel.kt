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
            it.uuid,
            it.categoryName,
            it.title,
            it.description,
            it.image,
            it.date,
            it.checked,
            it.resolved,
            it.userId,
        )
    }
}