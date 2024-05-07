package com.example.parking.di

import android.content.Context
import com.example.parking.data.db.ParkingDatabase
import com.example.parking.data.db.incidents.IncidentsDao
import com.example.parking.data.db.users.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    // Provides a singleton instance of ParkingDatabase
    @Singleton
    @Provides
    fun provideParkingDatabase(@ApplicationContext context: Context): ParkingDatabase {
        return ParkingDatabase.getInstance(context)
    }

    // Provides a singleton instance of IncidentsDao
    @Singleton
    @Provides
    fun provideIncidentsDao(database: ParkingDatabase): IncidentsDao {
        return database.incidentsDao()
    }

    // Provides a singleton instance of UsersDao
    @Singleton
    @Provides
    fun provideUsersDao(database: ParkingDatabase): UsersDao {
        return database.usersDao()
    }
}
