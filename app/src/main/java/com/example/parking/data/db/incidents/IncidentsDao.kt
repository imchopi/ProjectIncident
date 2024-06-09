package com.example.parking.data.db.incidents

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentsDao {

    /**
     * Inserts a list of incidents into the database, replacing any existing data with the same primary key.
     *
     * @param listIncidentsEntity The list of incidents to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listIncidentsEntity: List<IncidentsEntity>)

    /**
     * Inserts a single incident into the database, replacing any existing data with the same primary key.
     *
     * @param listIncidentsEntity The incident to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listIncidentsEntity: IncidentsEntity)

    /**
     * Retrieves all incidents from the database as a Flow, allowing for observation of changes.
     *
     * @return A Flow object containing a list of all incidents.
     */
    @Query("SELECT * FROM incidents")
    fun getIncidents(): Flow<List<IncidentsEntity>>

    /**
     * Retrieves an incident by its ID from the database as a Flow, allowing for observation of changes.
     *
     * @param uuid The unique identifier of the incident.
     * @return A Flow object containing the incident with the specified ID.
     */
    @Query("SELECT * FROM incidents WHERE uuid= :uuid")
    fun getIncidentByUuid(uuid: String): Flow<IncidentsEntity>
}

