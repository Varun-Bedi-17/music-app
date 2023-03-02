package com.example.musicbajao.viewModel.loginSinup.home

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicbajao.model.repository.UserRepo
import com.example.musicbajao.model.roomdatabase.UserEntity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*

class MyProfileViewModel(val context: Context) : ViewModel() {
    private val userRepo = UserRepo(context)

    var welcomeQuote = MutableLiveData<String>()
    var inputUsername = MutableLiveData<String>()
    var inputFirstname = MutableLiveData<String>()
    var inputPassword = MutableLiveData<String>()
    val inputProfileImage = MutableLiveData<Uri>()

    fun readLoginDataFromViewModel(username: String) {
        Log.d("User Info", username)
        var userInfo = UserEntity("dummy","dumm","j", "j")
        runBlocking {
            val result = viewModelScope.async(Dispatchers.IO) {
                userInfo = userRepo.readLoginDataFromRepo(username)!!
            }
            result.await()
        }

        welcomeQuote.value = "Welcome ${userInfo.name}!"
        inputUsername.value = userInfo.username
        inputFirstname.value = userInfo.name
        inputPassword.value = userInfo.pass
        inputProfileImage.value = Uri.parse(userInfo.profilePic)


    }

    fun updateData() {
        welcomeQuote.value = "Welcome ${inputFirstname.value.toString()}!"
        viewModelScope.launch(Dispatchers.IO) {
            userRepo.updateDataFromRepo(UserEntity(inputFirstname.value.toString(),inputUsername.value.toString(),inputPassword.value.toString(),inputProfileImage.value.toString()))
        }
    }


    fun addImageFromDatabase(circularProfile: CircleImageView) {
        circularProfile.setImageURI(inputProfileImage.value)

    }


}