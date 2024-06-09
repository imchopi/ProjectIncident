package com.example.parking.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.parking.data.db.incidents.IncidentsDao
import com.example.parking.data.db.incidents.IncidentsEntity
/*import com.example.parking.data.db.users.UsersDao
import com.example.parking.data.db.users.UsersEntity*/

/**
 * The Room database for this app, containing the `IncidentsEntity` and potentially other entities.
 *
 * This database uses the DAO pattern to access its data.
 *
 * @property incidentsDao The DAO for accessing incidents in the database.
 */
@Database(entities = [IncidentsEntity::class /*, UsersEntity::class*/], version = 5)
abstract class ParkingDatabase : RoomDatabase() {

    /**
     * Gets the DAO for incidents.
     *
     * @return The `IncidentsDao` for accessing incidents in the database.
     */
    abstract fun incidentsDao(): IncidentsDao

    companion object {
        @Volatile
        private var INSTANCE: ParkingDatabase? = null

        /**
         * Gets the singleton instance of the `ParkingDatabase`.
         *
         * This ensures that only one instance of the database is created at any given time.
         *
         * @param context The context to use for creating the database instance.
         * @return The singleton instance of the `ParkingDatabase`.
         */
        fun getInstance(context: Context): ParkingDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }

        /**
         * Builds the database.
         *
         * This method creates and configures the Room database.
         *
         * @param context The context to use for creating the database.
         * @return The created `ParkingDatabase` instance.
         */
        private fun buildDatabase(context: Context): ParkingDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ParkingDatabase::class.java,
                "parking_db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}
