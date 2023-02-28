package com.example.musicbajao.view.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build



class ApplicationClass : Application() {

    companion object {
        const val CHANNEL_ID = "channel 1"
        const val PLAY = "play_music"
        const val NEXT = "NEXT"
        const val PREVIOUS = "previous"
        const val EXIT = "exit"
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, "Playing song from Musicals", NotificationManager.IMPORTANCE_LOW )
            notificationChannel.description = "This channel is for Playing Songs by Musicals"

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}