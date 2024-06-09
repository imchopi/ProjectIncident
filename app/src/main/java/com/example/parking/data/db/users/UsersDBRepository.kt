/*package com.example.parking.data.db.users

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersDBRepository @Inject constructor(private val usersDao: UsersDao) {
    // Exposes a Flow of users from the database
    val users: Flow<List<UsersEntity>> = usersDao.getUsers()

    // Inserts a user into the database
    @WorkerThread
    suspend fun insert(listUsersEntity: UsersEntity) {
        usersDao.insert(listUsersEntity)
    }
}
*/