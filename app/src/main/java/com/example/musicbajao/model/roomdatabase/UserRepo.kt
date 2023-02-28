package com.example.musicbajao.model.roomdatabase

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.musicbajao.model.roomdatabase.UserDao
import com.example.musicbajao.model.roomdatabase.UserDatabase
import com.example.musicbajao.model.roomdatabase.UserEntity

class UserRepo(val context : Context) {

    private val userDao = UserDatabase.getDatabase(context).getDaoFromDB()
    fun getUserFromRepo() : LiveData<List<UserEntity>>{
        return userDao.getUsersFromDao()
    }

    suspend fun insertUserFromRepo(user : UserEntity) {
        return userDao.insertUserFromDao(user)
    }

    suspend fun deleteUserFromRepo(user : UserEntity) {
        return userDao.deleteUserFromDao(user)
    }

    suspend fun readLoginDataFromRepo(username : String) : UserEntity?{
        return userDao.readLoginDataFromDao(username)
    }

    suspend fun updateDataFromRepo(user : UserEntity){
        return userDao.updateUserFromDao(user)
    }
}