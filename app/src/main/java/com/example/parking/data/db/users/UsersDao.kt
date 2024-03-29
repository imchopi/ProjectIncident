package com.example.parking.data.db.users

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listUsersEntity: UsersEntity)

    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UsersEntity>>

}