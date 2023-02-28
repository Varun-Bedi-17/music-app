package com.example.musicbajao.viewModel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicbajao.model.repo.musicapi.data.Item
import com.example.musicbajao.model.repository.HomeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepo): ViewModel() {

    suspend fun getAllSongs(): List<Item> {
        var songsList : List<Item> = emptyList()
        viewModelScope.async(Dispatchers.IO) {
                songsList = repository.getAllSongs()
        }.await()
        return songsList
    }


}