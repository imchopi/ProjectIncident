package com.example.parking.di

import android.content.Context
import com.example.parking.data.api.AuthInterceptor
import com.example.parking.data.api.ParkingService
import com.example.parking.data.api.UserLogin
import com.example.parking.data.db.ParkingDatabase
import com.example.parking.data.db.incidents.IncidentsDao
import com.example.parking.data.db.users.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Credentials
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideParkingDatabase(@ApplicationContext context: Context): ParkingDatabase {
        return ParkingDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideIncidentsDao(database: ParkingDatabase): IncidentsDao {
        return database.incidentsDao()
    }

    @Singleton
    @Provides
    fun provideUsersDao(database: ParkingDatabase): UsersDao {
        return database.usersDao()
    }

    @Singleton
    @Provides
    fun provideParkingService(interceptor: AuthInterceptor): ParkingService {
        return ParkingService(interceptor)
    }

}