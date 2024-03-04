package com.example.parking.data.db.incidents

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.parking.data.db.users.User

@Entity(tableName = "incidents")
data class IncidentsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val status: String,
    val user: Int,
)

fun List<IncidentsEntity>.asIncident():List<Incident> {
    return this.map {
        Incident(
            it.id,
            it.title,
            it.description,
            it.status,
            it.user
        )
    }
}