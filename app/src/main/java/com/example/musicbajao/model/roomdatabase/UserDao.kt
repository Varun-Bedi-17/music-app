package com.example.musicbajao.model.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface UserDao {

    @Insert
    suspend fun insertUserFromDao(user : UserEntity)

    @Delete
    suspend fun deleteUserFromDao(user: UserEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUserFromDao(user: UserEntity)

    @Query("Select * from users")
    fun getUsersFromDao() : LiveData<List<UserEntity>>

    @Query("Select * from users where username LIKE :username")
    suspend fun readLoginDataFromDao(username : String) : UserEntity?
}