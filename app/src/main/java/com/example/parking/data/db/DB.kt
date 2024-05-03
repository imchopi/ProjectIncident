package com.example.parking.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.parking.data.db.incidents.IncidentsDao
import com.example.parking.data.db.incidents.IncidentsEntity
import com.example.parking.data.db.users.UsersDao
import com.example.parking.data.db.users.UsersEntity

@Database(entities = [IncidentsEntity::class, UsersEntity::class], version = 3)
abstract class ParkingDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun incidentsDao(): IncidentsDao

    companion object {
        @Volatile
        private var INSTANCE: ParkingDatabase? = null

        fun getInstance(context: Context): ParkingDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }

        private fun buildDatabase(context: Context): ParkingDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ParkingDatabase::class.java,
                "parking_db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}
