package com.example.parking.data.repository

import android.util.Log
import com.example.parking.data.db.incidents.Incident
import com.example.parking.data.db.incidents.IncidentsDBRepository
import com.example.parking.data.db.incidents.IncidentsEntity
import com.example.parking.data.db.incidents.asIncident
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val incidentsDBRepository: IncidentsDBRepository,
) {
    // Exposes a Flow of Incidents to observe changes
    val incident: Flow<List<Incident>>
        get(){
            // Maps the Flow of IncidentsEntity to a Flow of Incident using the extension function asIncident()
            val list = incidentsDBRepository.incidents.map {
                it.asIncident()
            }
            // Logs the result for debugging
            Log.d("TemillaTemaTema", "El tema: $list")
            return list
        }

    // Adds an incident to the database
    suspend fun addIncident(incident: IncidentsEntity){
        incidentsDBRepository.insert(incident)
    }
}
