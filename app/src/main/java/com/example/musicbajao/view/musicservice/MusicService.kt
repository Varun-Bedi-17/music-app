package com.example.musicbajao.view.musicservice

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.musicbajao.R
import com.example.musicbajao.view.application.ApplicationClass
import com.example.musicbajao.view.activity.MusicPlayerActivity


class MusicService : Service() {

    private var myBinder = MyBinder()
    var mediaPlayer : MediaPlayer? = null
    private lateinit var mediaSession : MediaSessionCompat

    override fun onBind(intent: Intent): IBinder? {
        mediaSession = MediaSessionCompat(baseContext, "My Music MediaSession")
        return myBinder
    }


    inner class MyBinder : Binder(){
        fun currentService(): MusicService {
            return this@MusicService
        }

    }


    fun showNotification(playPauseBtn : Int){

        val prevIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext, 0, prevIntent, PendingIntent.FLAG_IMMUTABLE)

        val nextIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE)

        val playIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(baseContext, 0, playIntent, PendingIntent.FLAG_IMMUTABLE)

        val exitIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(baseContext, 0, exitIntent, PendingIntent.FLAG_IMMUTABLE)


        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(MusicPlayerActivity.songsList[MusicPlayerActivity.songPosition].audioName)
            .setContentText(MusicPlayerActivity.songsList[MusicPlayerActivity.songPosition].audioArtist)
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(MusicPlayerActivity.songsList[MusicPlayerActivity.songPosition].audioImage)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_previous, "Previous", prevPendingIntent)
            .addAction(playPauseBtn, "Play", playPendingIntent)
            .addAction(R.drawable.ic_noti_next, "Next", nextPendingIntent)
            .addAction(R.drawable.ic_noti_cancel, "Exit", exitPendingIntent)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0,1,2,3))
            .build()

        startForeground(13, notification)
    }
}