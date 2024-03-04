package com.example.parking.data.repository

import com.example.parking.data.api.ParkingRepository
import com.example.parking.data.api.asEntityModel
import com.example.parking.data.db.incidents.Incident
import com.example.parking.data.db.incidents.IncidentsDBRepository
import com.example.parking.data.db.incidents.asIncident
import com.example.parking.data.db.users.UsersDBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val usersDBRepository: UsersDBRepository,
    private val incidentsDBRepository: IncidentsDBRepository,
    private val apiRepository: ParkingRepository
) {
    val incident: Flow<List<Incident>>
        get(){
            val list = incidentsDBRepository.incidents.map {
                it.asIncident()
            }
            return list
        }

    suspend fun refreshList() {
        withContext(Dispatchers.IO) {
            try {
                val apiParking = apiRepository.getIncidents()
                incidentsDBRepository.insert(apiParking.asEntityModel())
            } catch (e: Exception) {
                // Manejar cualquier excepción
            }
        }
    }

    suspend fun getIncident(id: Int) = incidentsDBRepository.getIncidentById(id)

}
