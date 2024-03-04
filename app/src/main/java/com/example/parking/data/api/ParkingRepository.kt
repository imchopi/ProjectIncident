package com.example.parking.data.api

import android.util.Log
import com.example.parking.data.db.incidents.Incident
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParkingRepository @Inject constructor(private val service: ParkingService) {

    suspend fun getIncidents(): List<Incident> {
        try {

            val apiService = service.api.getIncidents()
            Log.d("GET INCIDENT", "incidents: $apiService")

            val incident = apiService.map { incident ->
                Incident(
                    incident.id,
                    incident.title,
                    incident.description,
                    incident.status,
                    incident.user
                )
            }

            return incident

        } catch (e: Exception) {
            Log.d("Error", "CARGANDO INCIDENTES: $e")
            return emptyList()
        }
    }
}