package com.example.musicbajao.model.repository

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.musicbajao.model.repo.musicapi.data.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class HomeRepo(private val context: Context) {

    private var songList: List<Item> = emptyList()

    suspend fun getAllSongs(): List<Item> {
        val token = getToken()
        if (token != null) {
            val musicApiAlbum = MusicHelper.getInstance(context).create(MusicApi::class.java)
            try {
                val response = musicApiAlbum.getAlbums()
                if (response.isSuccessful) {
                    songList = response.body()!!.tracks.items

                    Log.d("Tokenss", response.code().toString())
                    Log.d("tracks", response.body()!!.tracks.items[0].track.artists.toString())
                    Log.d("tracks", response.body()!!.tracks.items[0].track.duration_ms.toString())
                    Log.d("tracks", response.body()!!.tracks.items[0].track.href.toString())
                    Log.d("tracks", response.body()!!.tracks.items[0].track.name.toString())
                    Log.d(
                        "tracks",
                        response.body()?.tracks?.items?.get(0)?.track?.preview_url.toString()
                    )
                } else {
                    when (response.code()) {
                        401 -> {
                            getTokenFromServer()
                        }
                        else -> {
                            Log.i("Response", "Not 401")
                        }
                    }
                }
                Log.d("Checking ", "api hit")


            } catch (e: Exception) {
                Log.d("Error", e.toString())
//                throw NoConnectivityException()
            }

        } else {
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




//call.enqueue(object : Callback<AudioModelApi> {
//    override fun onResponse(
//        call: Call<AudioModelApi>,
//        response: Response<AudioModelApi>
//    ) {
//        if (response.isSuccessful) {
//            Log.d("tracks", "Response")
//            Log.d("Tokenss", response.code().toString())
//
//            Log.d(
//                "tracks",
//                response.body()!!.tracks.items[0].track.artists.toString()
//            )
//            Log.d(
//                "tracks",
//                response.body()!!.tracks.items[0].track.duration_ms.toString()
//            )
//            Log.d("tracks", response.body()!!.tracks.items[0].track.href.toString())
//            Log.d("tracks", response.body()!!.tracks.items[0].track.name.toString())
//            Log.d(
//                "tracks",
//                response.body()?.tracks?.items?.get(0)?.track?.preview_url.toString()
//            )
//
//            songList = response.body()!!.tracks.items
//            songList.forEach {
//                Log.d("track", it.track.name)
//            }
//
//        } else {
//            when (response.code()) {
//                401 -> {
//                    getTokenFromServer()
//                    call.clone().enqueue(this)
//                }
//                else -> {
//                    Log.i("Response", "Not 401")
//                }
//            }
//
//        }
//    }
//
//    override fun onFailure(call: Call<AudioModelApi>, t: Throwable) {
//        Log.d("onfailure", t.message.toString())
//    }
//
//})


