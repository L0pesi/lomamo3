package com.lomamo.lomamo3.database.repos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lomamo.lomamo3.database.models.UserData
import kotlinx.coroutines.flow.Flow

@Dao
interface IUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(user: UserData)

    @Query("SELECT * FROM User")
    fun get(): Flow<UserData>


    @Query("DELETE FROM User")
    suspend fun deleteAll()
}