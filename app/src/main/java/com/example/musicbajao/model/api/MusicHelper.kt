package com.example.musicbajao.model.api

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object MusicHelper {

    private const val BASE_URL = "https://api.spotify.com/"


    fun getInstance(context: Context): Retrofit {

        val builder = OkHttpClient().newBuilder()
        builder.readTimeout(60, TimeUnit.SECONDS)
        builder.connectTimeout(60, TimeUnit.SECONDS)
        builder.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer ${getToken(context).toString()}")
                .build()

            chain.proceed(request)
        }


        val client = builder.build()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build()
    }

    private fun getToken(context: Context): String? {
        val preference = context.getSharedPreferences("token", AppCompatActivity.MODE_PRIVATE)
        return preference.getString("token", null)
    }

}
