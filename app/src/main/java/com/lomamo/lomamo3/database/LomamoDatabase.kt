package com.lomamo.lomamo3.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lomamo.lomamo3.database.models.MovieData
import com.lomamo.lomamo3.database.models.UserData
import com.lomamo.lomamo3.database.repos.IMovieDao
import com.lomamo.lomamo3.database.repos.IUserDao

@Database(
    entities = [UserData::class, MovieData::class],
    version = 1,
    exportSchema = true
)
abstract class LomamoDatabase : RoomDatabase() {
    abstract fun userDao(): IUserDao
    abstract fun movieDao(): IMovieDao

    companion object {

        @Volatile
        private var INSTANCE: LomamoDatabase? = null

        fun getDatabase(context: Context): LomamoDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            if (INSTANCE == null) {
                synchronized(this) {
                    // Pass the database to the INSTANCE
                    INSTANCE = buildDatabase(context)
                }
            }
            // Return database.
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): LomamoDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                LomamoDatabase::class.java,
                "lomamo_database"
            )
                .build()
        }
    }
}