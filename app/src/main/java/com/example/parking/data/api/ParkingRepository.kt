package com.example.parking.data.api

import android.util.Log
import com.example.parking.data.db.incidents.Incident
import com.example.parking.data.db.incidents.IncidentsEntity
import com.example.parking.data.db.users.User
import com.example.parking.data.db.users.UsersEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParkingRepository @Inject constructor(private val service: ParkingService) {

    suspend fun getIncidents(): List<Incident> {
        try {

            val apiService = service.api.getIncidents(300, 0)
            Log.d("GET INCIDENT", "incidents: $apiService")

            val incident = apiService.map { incident ->
                Incident(
                    incident.id,
                    incident.title,
                    incident.description,
                    incident.status,
                    incident.user,
                    incident.image,
                    incident.imageType
                )
            }

            Log.d("GET INCIDENT 2", "incidents: $incident")
            return incident

        } catch (e: Exception) {
            Log.d("Error", "CARGANDO INCIDENTES: $e")
            return emptyList()
        }
    }

}