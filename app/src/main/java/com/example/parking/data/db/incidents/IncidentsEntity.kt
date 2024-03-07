package com.example.parking.data.db.incidents

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incidents")
data class IncidentsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 2,
    val title: String,
    val description: String,
    val status: String,
    val user: Int,
    val image: String? = "",
    val imageType: String? = ""
)

fun List<IncidentsEntity>.asIncident():List<Incident> {
    return this.map {
        Incident(
            it.id,
            it.title,
            it.description,
            it.status,
            it.user,
            it.image,
            it.imageType
        )
    }
}