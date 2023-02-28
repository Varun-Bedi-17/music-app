package com.example.musicbajao.view.activity

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicbajao.R
import com.example.musicbajao.databinding.ActivityMusicPlayerBinding
import com.example.musicbajao.model.repo.musicapi.data.Item

class MusicPlayerActivityApi : AppCompatActivity(), View.OnClickListener {
    companion object {
        lateinit var songsList: ArrayList<Item>
        var songPosition = -1
        var isPlaying: Boolean = true

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: ActivityMusicPlayerBinding
        var mediaPlayer: MediaPlayer? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songsList = intent.getSerializableExtra("audioApi") as ArrayList<Item>
        songPosition = intent.getIntExtra("positionApi", -1)
        mediaPlayer = null
        attachListeners()
        playSong()
    }

    private fun playSong() {

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()

            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)

            mediaPlayer?.setDataSource(songsList[songPosition].track.preview_url)
            mediaPlayer?.prepare()
        }


        mediaPlayer?.start()
        initializeLayout()
        enableSeekBar()
        seekBarUpdater()

    }


    private fun initializeLayout() {
        if (songsList[songPosition] != null) {
            binding.songName.text = songsList[songPosition].track.name
            binding.songEndPosition.text =
                convertDurationToMinutesString(songsList[songPosition].track.duration_ms)

            Glide.with(this).load(songsList[songPosition].track.album.images[0].url)
                .apply(RequestOptions.placeholderOf(R.drawable.music_logo))
                .into(binding.musicImageLogo)

            binding.playBtn.setImageResource(R.drawable.ic_pause_foreground)
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
                    pausePlaying()
                    binding.playBtn.setImageResource(R.drawable.ic_play_button_back_foreground)
                    isPlaying = false
                } else {
                    playSong()
                    isPlaying = true
                }
            }

            binding.nextBtn -> {
                prevNextSongApi(true)
                mediaPlayer?.stop()
                mediaPlayer = null
                playSong()
            }

            binding.prevBtn -> {
                prevNextSongApi(false)
                mediaPlayer?.stop()
                mediaPlayer = null
                playSong()
            }

        }
    }

    private fun prevNextSongApi(isNext: Boolean) {
        if (isNext) {
            if (songPosition == songsList.size - 1) {
                songPosition = 0
            } else {
                songPosition++
            }
        } else {
            if (songPosition == 0) {
                songPosition = songsList.size - 1
            } else {
                songPosition--
            }
        }

    }

    private fun pausePlaying() {
        mediaPlayer?.pause()
    }

    private fun enableSeekBar() {
        binding.seekBar.max = songsList[songPosition].track.duration_ms


        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
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
                binding.seekBar.progress = mediaPlayer!!.currentPosition
                binding.startPosition.text =
                    convertDurationToMinutesString(mediaPlayer!!.currentPosition)
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

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
    }


}


