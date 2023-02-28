package com.example.musicbajao.model.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TokenHelper {
    private const val BASE_URL = "https://accounts.spotify.com/api/"

    fun getInstance() : Retrofit {
        return  Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}