package com.lomamo.lomamo3.database.repos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lomamo.lomamo3.database.models.MovieData
import kotlinx.coroutines.flow.Flow

@Dao
interface IMovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(note: MovieData)

    @Query("SELECT * FROM Movie")
    fun get(): Flow<List<MovieData>>

    @Query("DELETE FROM Movie")
    suspend fun deleteAll()
}