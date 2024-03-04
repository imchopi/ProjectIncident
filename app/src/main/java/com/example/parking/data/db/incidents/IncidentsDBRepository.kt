package com.example.parking.data.db.incidents

import androidx.annotation.WorkerThread
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncidentsDBRepository @Inject constructor(private val incidentDao: IncidentsDao) {
    val incidents: Flow<List<IncidentsEntity>> = incidentDao.getIncidents()

    @WorkerThread
    suspend fun insert(listIncidentEntity: List<IncidentsEntity>) {
        incidentDao.insert(listIncidentEntity)
    }

    @WorkerThread
    suspend fun getIncidentById(id: Int): Flow<IncidentsEntity> {
        return incidentDao.getIncidentById(id)
    }

}