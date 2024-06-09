package com.example.parking.data.db.incidents

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncidentsDBRepository @Inject constructor(private val incidentDao: IncidentsDao) {

    /**
     * Exposes a Flow of incidents from the database.
     *
     * This allows observing the list of incidents and reacting to changes in the database.
     */
    val incidents: Flow<List<IncidentsEntity>> = incidentDao.getIncidents()

    /**
     * Inserts a single incident into the database.
     *
     * @param listIncidentEntity The incident to be inserted.
     */
    @WorkerThread
    suspend fun insert(listIncidentEntity: IncidentsEntity) {
        incidentDao.insert(listIncidentEntity)
    }

    /**
     * Inserts a list of incidents into the database.
     *
     * @param listIncidentEntity The list of incidents to be inserted.
     */
    @WorkerThread
    suspend fun insertAll(listIncidentEntity: List<IncidentsEntity>) {
        incidentDao.insertAll(listIncidentEntity)
    }

    /**
     * Retrieves an incident by its ID from the database as a Flow, allowing for observation of changes.
     *
     * @param uuid The unique identifier of the incident.
     * @return A Flow object containing the incident with the specified ID.
     */
    @WorkerThread
    suspend fun getIncidentByUuid(uuid: String): Flow<IncidentsEntity> {
        return incidentDao.getIncidentByUuid(uuid)
    }
}

