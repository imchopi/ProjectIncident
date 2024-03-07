package com.example.parking.data.db.incidents

import androidx.annotation.WorkerThread
import com.example.parking.data.db.users.UsersDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IncidentsDBRepository @Inject constructor(private val incidentDao: IncidentsDao) {
    val incidents: Flow<List<IncidentsEntity>> = incidentDao.getIncidents()


    @WorkerThread
    suspend fun insert(listIncidentEntity: IncidentsEntity) {
        incidentDao.insert(listIncidentEntity)
    }

    @WorkerThread
    suspend fun insertAll(listIncidentEntity: List<IncidentsEntity>) {
        incidentDao.insertAll(listIncidentEntity)
    }

    @WorkerThread
    suspend fun getIncidentById(id: Int): Flow<IncidentsEntity> {
        return incidentDao.getIncidentById(id)
    }

}