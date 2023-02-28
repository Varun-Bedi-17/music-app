package com.example.musicbajao.view.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.widget.SeekBar
import com.example.musicbajao.R
import com.example.musicbajao.databinding.ActivityMusicPlayerBinding
import com.example.musicbajao.model.AudioModel
import com.example.musicbajao.view.musicservice.MusicService

class MusicPlayerActivity : AppCompatActivity(), View.OnClickListener, ServiceConnection {
    companion object {
        lateinit var songsList: ArrayList<AudioModel>
        var songPosition = -1
        var isPlaying: Boolean = true

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityMusicPlayerBinding
        var musicService: MusicService? = null
        var stopPlayer = false


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songsList = intent.getSerializableExtra("audiolist") as ArrayList<AudioModel>
        songPosition = intent.getIntExtra("position", -1)

        // For starting service
        val intentForService = Intent(this, MusicService::class.java)
        bindService(intentForService, this, BIND_AUTO_CREATE)
        startService(intentForService)

        attachListeners()


    }

    private fun startPlaying() {

        // When enters first time in activity
        if (musicService!!.mediaPlayer == null) {
            musicService!!.mediaPlayer =
                MediaPlayer.create(this, Uri.parse(songsList[songPosition].audioPath))
            musicService!!.mediaPlayer!!.start()

        }

        // if music player is stopped using back button
        else if (stopPlayer) {
            musicService!!.mediaPlayer =
                MediaPlayer.create(this, Uri.parse(songsList[songPosition].audioPath))
            musicService!!.mediaPlayer!!.start()
            stopPlayer = false
        } else {
            musicService!!.mediaPlayer!!.start()
        }

        initializeLayout()
        enableSeekBar()
        seekBarUpdater()
        musicService!!.showNotification(R.drawable.ic_noti_pause)
        if (isPlaying) {
            binding.playBtn.setImageResource(R.drawable.ic_pause_foreground)
            isPlaying = false
            musicService!!.showNotification(R.drawable.ic_noti_pause)
        }

    }

    private fun initializeLayout() {
        if (songsList[songPosition] != null) {
            binding.songName.text = songsList[songPosition].audioName
            binding.songEndPosition.text =
                convertDurationToMinutesString(musicService!!.mediaPlayer!!.duration)
            binding.musicImageLogo.setImageURI(Uri.parse(songsList[songPosition].audioImage))
        } else {
            binding.songName.text = "No audio found"
        }
    }

    private fun attachListeners() {
        binding.playBtn.setOnClickListener(this)
        binding.prevBtn.setOnClickListener(this)
        binding.nextBtn.setOnClickListener(this)
    }


    override fun onClick(btn: View?) {
        when (btn) {
            binding.playBtn -> {
                if (isPlaying) {
                    startPlaying()
                } else {
                    pausePlaying()
                    binding.playBtn.setImageResource(R.drawable.ic_play_button_back_foreground)
                    isPlaying = true
                }
            }

            binding.nextBtn -> {
                prevNextSong(true)
                musicService!!.mediaPlayer!!.stop()
                musicService!!.mediaPlayer = null
                startPlaying()
            }

            binding.prevBtn -> {
                prevNextSong(false)
                musicService!!.mediaPlayer!!.stop()
                musicService!!.mediaPlayer = null
                startPlaying()
            }

        }
    }


    private fun stopPlaying() {
        if (musicService!!.mediaPlayer != null) {
            musicService!!.mediaPlayer!!.stop()
            stopPlayer = true
            isPlaying = true
        }
    }

    private fun pausePlaying() {
        if (musicService!!.mediaPlayer != null) {
            musicService!!.mediaPlayer!!.pause()
            musicService!!.showNotification(R.drawable.ic_noti_play)
        }
    }

    private fun enableSeekBar() {
        binding.seekBar.max = musicService!!.mediaPlayer!!.duration


        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    musicService!!.mediaPlayer!!.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    private fun seekBarUpdater() {
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                binding.seekBar.progress = musicService!!.mediaPlayer!!.currentPosition
                binding.startPosition.text =
                    convertDurationToMinutesString(musicService!!.mediaPlayer!!.currentPosition)
                handler.postDelayed(this, 50)
            }
        }
        handler.post(runnable)
    }


    private fun convertDurationToMinutesString(duration: Int): String {
        val minutes = duration / 1000 / 60
        val seconds = (duration / 1000 % 60).toString().padStart(2, '0')
        return "$minutes:$seconds"
    }

//    override fun onBackPressed() {
//        stopPlaying()
//        musicService!!.stopForeground(true)
//        super.onBackPressed()
//
//    }

    override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder

        if (musicService != null) {
            stopPlaying()
        }

        musicService = binder.currentService()


        // Play song after service starts
        startPlaying()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }


}

fun prevNextSong(next: Boolean) {

    if (next) {
        if (MusicPlayerActivity.songPosition == MusicPlayerActivity.songsList.size - 1) {
            MusicPlayerActivity.songPosition = 0
        } else ++MusicPlayerActivity.songPosition
    } else {
        if (MusicPlayerActivity.songPosition == 0) {
            MusicPlayerActivity.songPosition = MusicPlayerActivity.songsList.size - 1
        } else --MusicPlayerActivity.songPosition
    }

}


