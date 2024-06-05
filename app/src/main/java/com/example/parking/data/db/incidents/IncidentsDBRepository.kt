package com.example.parking.data.db.incidents

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncidentsDBRepository @Inject constructor(private val incidentDao: IncidentsDao) {
    // Exposes a Flow of incidents from the database
    val incidents: Flow<List<IncidentsEntity>> = incidentDao.getIncidents()

    // Inserts a single incident into the database
    @WorkerThread
    suspend fun insert(listIncidentEntity: IncidentsEntity) {
        incidentDao.insert(listIncidentEntity)
    }

    // Inserts a list of incidents into the database
    @WorkerThread
    suspend fun insertAll(listIncidentEntity: List<IncidentsEntity>) {
        incidentDao.insertAll(listIncidentEntity)
    }

    // Retrieves an incident by its ID from the database as a Flow, allowing for observation of changes
    @WorkerThread
    suspend fun getIncidentByUuid(uuid: String): Flow<IncidentsEntity> {
        return incidentDao.getIncidentByUuid(uuid)
    }
}
