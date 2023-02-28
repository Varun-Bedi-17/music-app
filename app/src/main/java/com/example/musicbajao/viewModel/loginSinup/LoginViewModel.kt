package com.example.musicbajao.viewModel.loginSinup

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicbajao.model.roomdatabase.UserRepo
import com.example.musicbajao.model.roomdatabase.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel(val context: Context) : ViewModel(){
    private val userRepo = UserRepo(context)
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var inputUsername = MutableLiveData<String>()
    var inputPassword = MutableLiveData<String>()
    var check : Boolean = false
    var userNotAvailable = MutableLiveData<Boolean>()
    var navigateToHomeScreen = MutableLiveData<Boolean>()
    var inValidPassword = MutableLiveData<Boolean>()
    var emptyUsername = MutableLiveData<Boolean>()
    var emptyPassword = MutableLiveData<Boolean>()


    fun getUserFromViewModel() : LiveData<List<UserEntity>>{
        return userRepo.getUserFromRepo()
    }

    fun insertUserFromViewModel(user : UserEntity){
        viewModelScope.launch(Dispatchers.IO){
            userRepo.insertUserFromRepo(user)
        }
    }

    fun deleteUserFromViewModel(user : UserEntity){
        viewModelScope.launch(Dispatchers.IO){
            userRepo.deleteUserFromRepo(user)
        }
    }

    fun readLoginDataFromViewModel(username : String) : Boolean {
        if(inputUsername.value == null){
            emptyUsername.value = true
        }
        else if(inputPassword.value == null){
            emptyPassword.value = true
        }
        else {
            viewModelScope.launch {
                var userInfo = userRepo.readLoginDataFromRepo(username)
                if (userInfo?.username != null) {
                    if (userInfo?.pass == inputPassword.value) {
                        val preferenceForLogin = context.getSharedPreferences("isLogin", AppCompatActivity.MODE_PRIVATE)
                        val preferenceForLoginEdit = preferenceForLogin.edit()
                        preferenceForLoginEdit.putString("userId", userInfo.username)
                        preferenceForLoginEdit.apply()

                        navigateToHomeScreen.value = true
                    } else {
                        inValidPassword.value = true
                    }
                } else {
                    userNotAvailable.value = true
                }
            }
        }
        return check
    }





}