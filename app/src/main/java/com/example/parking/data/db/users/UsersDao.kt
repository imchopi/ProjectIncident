/*package com.example.parking.data.db.users

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {
    // Inserts a user into the database, replacing any existing data with the same primary key
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listUsersEntity: UsersEntity)

    // Retrieves all users from the database as a Flow, allowing for observation of changes
    @Query("SELECT * FROM users")
    fun getUsers(): Flow<List<UsersEntity>>
}*/
