package com.example.musicbajao.model.repository

import com.google.gson.annotations.SerializedName

data class TokenModel(
    @SerializedName("access_token")
    val accessToken: String
)
