package com.example.musicbajao.viewModel.loginSinup.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.musicbajao.model.repository.HomeRepo
import com.example.musicbajao.viewModel.home.HomeViewModel

class HomeViewModelFactory(private val repo: HomeRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return HomeViewModel(repo) as T
    }
}