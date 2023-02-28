package com.example.musicbajao.model.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity (
        val name : String,
        @PrimaryKey
        val username : String,

        val pass : String,
        val profilePic : String
)