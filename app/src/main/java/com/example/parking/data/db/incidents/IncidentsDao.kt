package com.example.parking.data.db.incidents

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentsDao {
    // Inserts a list of incidents into the database, replacing any existing data with the same primary key
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listIncidentsEntity: List<IncidentsEntity>)

    // Inserts a single incident into the database, replacing any existing data with the same primary key
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listIncidentsEntity: IncidentsEntity)

    // Retrieves all incidents from the database as a Flow, allowing for observation of changes
    @Query("SELECT * FROM incidents")
    fun getIncidents(): Flow<List<IncidentsEntity>>

    // Retrieves an incident by its ID from the database as a Flow, allowing for observation of changes
    @Query("SELECT * FROM incidents WHERE id= :id")
    fun getIncidentById(id: Int): Flow<IncidentsEntity>
}
