package com.example.parking.data.db.incidents

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listIncidentsEntity: List<IncidentsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listIncidentsEntity: IncidentsEntity)

    @Query("SELECT * FROM incidents")
    fun getIncidents(): Flow<List<IncidentsEntity>>


    @Query("SELECT * FROM incidents WHERE id= :id")
    fun getIncidentById(id: Int): Flow<IncidentsEntity>
}