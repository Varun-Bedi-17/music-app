package com.example.musicbajao.model.api

import com.example.musicbajao.model.TokenModel
import com.example.musicbajao.model.repo.musicapi.data.AudioModelApi
import retrofit2.Response
import retrofit2.http.*

interface MusicApi {
    @Headers(
        "Authorization:Basic Y2Y0Zjc5ZWI5YTNiNGZkYzkwOWU2NzdkZGY3YmI0Mjc6OGQ2MDhmZTM4MTgxNGVhYWFlYTM4MTIyOWY5N2UwY2Q=",
        "Content-Type:application/x-www-form-urlencoded"
    )
    @FormUrlEncoded
    @POST("token")
    suspend fun getAccessToken(@Field("grant_type") grantType: String) : Response<TokenModel>


    @GET("v1/playlists/37i9dQZF1DX14CbVHtvHRB")
    suspend fun getAlbums(): Result<AudioModelApi>
}