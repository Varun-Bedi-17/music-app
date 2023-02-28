package com.example.musicbajao.view.musicservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import com.example.musicbajao.R
import com.example.musicbajao.view.application.ApplicationClass
import com.example.musicbajao.view.activity.MusicPlayerActivity
import com.example.musicbajao.view.activity.prevNextSong
import kotlin.system.exitProcess

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context : Context?, intent : Intent?) {
        when(intent?.action){
            ApplicationClass.PREVIOUS-> previousNextSong(false, context)
            ApplicationClass.PLAY-> if (MusicPlayerActivity.isPlaying) playSong() else pauseSong()
            ApplicationClass.NEXT-> previousNextSong(true, context)
            ApplicationClass.EXIT-> {
                MusicPlayerActivity.musicService!!.stopForeground(true)
                MusicPlayerActivity.musicService = null
                exitProcess(0)
            }
        }
    }

    private fun playSong() {
        MusicPlayerActivity.isPlaying = false
        MusicPlayerActivity.musicService!!.mediaPlayer!!.start()
        MusicPlayerActivity.musicService!!.showNotification(R.drawable.ic_noti_pause)
        MusicPlayerActivity.binding.playBtn.setImageResource(R.drawable.ic_pause_foreground)
    }

    private fun pauseSong() {
        MusicPlayerActivity.isPlaying = true
        MusicPlayerActivity.musicService!!.mediaPlayer!!.pause()
        MusicPlayerActivity.musicService!!.showNotification(R.drawable.ic_noti_play)
        MusicPlayerActivity.binding.playBtn.setImageResource(R.drawable.ic_play_button_back_foreground)
    }

    private fun previousNextSong(next : Boolean, context: Context?) {
        if (next){
            prevNextSong(true)
        }
        else{
            prevNextSong(false)
        }

        MusicPlayerActivity.musicService!!.showNotification(R.drawable.ic_noti_pause)

        // To change layout in music player
        MusicPlayerActivity.binding.songName.text = MusicPlayerActivity.songsList[MusicPlayerActivity.songPosition].audioName
        MusicPlayerActivity.binding.songEndPosition.text = convertDurationToMinutesString(
            MusicPlayerActivity.musicService!!.mediaPlayer!!.duration)
        MusicPlayerActivity.binding.musicImageLogo.setImageURI(Uri.parse(MusicPlayerActivity.songsList[MusicPlayerActivity.songPosition].audioImage))

        // To stop current song
        MusicPlayerActivity.musicService!!.mediaPlayer!!.stop()
        MusicPlayerActivity.musicService!!.mediaPlayer = null

        // Create media player for next and previous song
        MusicPlayerActivity.musicService!!.mediaPlayer = MediaPlayer.create(context, Uri.parse(
            MusicPlayerActivity.songsList[MusicPlayerActivity.songPosition].audioPath))

        // To play song
        playSong()
    }

    private fun convertDurationToMinutesString(duration: Int): String {
        val minutes = duration / 1000 / 60
        val seconds = (duration / 1000 % 60).toString().padStart(2, '0')
        return "$minutes:$seconds"
    }
}