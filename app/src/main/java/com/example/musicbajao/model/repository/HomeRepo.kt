package com.example.musicbajao.model.repository

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.musicbajao.model.api.MusicApi
import com.example.musicbajao.model.api.MusicHelper
import com.example.musicbajao.model.api.TokenHelper
import com.example.musicbajao.model.repo.musicapi.data.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class HomeRepo(private val context: Context) {

    private var songList: List<Item> = emptyList()

    suspend fun getAllSongs(): List<Item> {
        val token = getToken()
        if (token != null) {
            val musicApiAlbum = MusicHelper.getInstance(context).create(MusicApi::class.java)
            val result = musicApiAlbum.getAlbums()
            result.onSuccess {
                Log.d("Success state", it.toString())
                songList = it.tracks.items

            }.onFailure {
                Log.d("Network error", it.toString())
                if (it is IOException) {
                    return emptyList()
                } else if (it is HttpException) {
                    getTokenFromServer()
                } else {
                    Log.d("Error in calling api", it.toString())
                    return emptyList()
                }

            }
        }else {
            getTokenFromServer()
        }
        return songList
    }

    private fun getToken(): String? {
        val preference = context.getSharedPreferences("token", AppCompatActivity.MODE_PRIVATE)
        return preference.getString("token", null)
    }


    private fun getTokenFromServer() {
        val musicAPItoken = TokenHelper.getInstance().create(MusicApi::class.java)

        var accesTokenString: String
        GlobalScope.launch(Dispatchers.IO) {
            val result = musicAPItoken.getAccessToken("client_credentials")
            Log.d("TokenForScope", result.body().toString())

            accesTokenString = result.body()?.accessToken.toString()
            val preferenceForToken =
                context.getSharedPreferences("token", AppCompatActivity.MODE_PRIVATE).edit()
            preferenceForToken.putString("token", accesTokenString)
            preferenceForToken.apply()
            getAllSongs()
        }
    }

}



