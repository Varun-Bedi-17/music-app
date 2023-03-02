package com.example.musicbajao.viewModel.loginSinup

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicbajao.R
import com.example.musicbajao.model.repository.UserRepo
import com.example.musicbajao.model.roomdatabase.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignupViewModel(val context: Context) : ViewModel() {
    private val userRepo = UserRepo(context)
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var inputUsername = MutableLiveData<String>()
    var inputPassword = MutableLiveData<String>()
    var inputFirstname = MutableLiveData<String>()
    var inputConfirmPassword = MutableLiveData<String>()
    var check: Boolean = false

    var userAlreadyAvailable = MutableLiveData<Boolean>()
    var navigateToSignupsSplashScreen = MutableLiveData<Boolean>()
    var inValidPassword = MutableLiveData<Boolean>()
    var emptyFirstname = MutableLiveData<Boolean>()
    var emptyUsername = MutableLiveData<Boolean>()
    var emptyPassword = MutableLiveData<Boolean>()
    var emptyConfirmPass = MutableLiveData<Boolean>()

    fun getUserFromViewModel(): LiveData<List<UserEntity>> {
        return userRepo.getUserFromRepo()
    }

    fun insertUserFromViewModel(user: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepo.insertUserFromRepo(user)
        }
    }

    fun readLoginDataFromViewModel(username: String): Boolean {
        if (inputFirstname.value == null) {
            emptyFirstname.value = true
        } else if (inputUsername.value == null) {
            emptyUsername.value = true
        } else if (inputPassword.value == null) {
            emptyPassword.value = true
        } else if (inputConfirmPassword.value == null) {
            emptyConfirmPass.value = true
        } else {
            viewModelScope.launch {
                var userInfo = userRepo.readLoginDataFromRepo(username)
                if (userInfo?.username == null) {
                    if (inputConfirmPassword.value == inputPassword.value) {
                        insertUserFromViewModel(
                            UserEntity(
                                inputFirstname.value.toString(),
                                inputUsername.value.toString(),
                                inputPassword.value.toString(),
                                "android.resource://${context.packageName}/${R.drawable.default_user_logo}"
                            )
                        )
                        navigateToSignupsSplashScreen.value = true
                    } else {
                        inValidPassword.value = true
                    }
                } else {
                    userAlreadyAvailable.value = true
                }
            }
        }
        return check
    }
}