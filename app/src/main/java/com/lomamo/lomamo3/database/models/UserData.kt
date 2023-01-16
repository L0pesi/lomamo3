package com.lomamo.lomamo3.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "User")
data class UserData(
    @PrimaryKey
    @ColumnInfo(name = "uniqueId")
    val uniqueId: String
)