package com.example.parking.data.db.users

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersDBRepository @Inject constructor(private val usersDao: UsersDao) {
    val users: Flow<List<UsersEntity>> = usersDao.getUsers()

    @WorkerThread
    suspend fun insert(listUsersEntity: UsersEntity) {
        usersDao.insert(listUsersEntity)
    }

    @WorkerThread
    suspend fun getUserById(id: Int): Flow<UsersEntity> {
        return usersDao.getUserById(id)
    }

}