package com.example.musicbajao.viewModel.home

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicbajao.model.AudioModel

class DownloadsViewModel : ViewModel() {

    private val allAudio = MutableLiveData<ArrayList<AudioModel>>()

    fun getAllAudioFromDevice(context: Context): LiveData<ArrayList<AudioModel>> {
        val tempAudioList = ArrayList<AudioModel>()
        val internalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.ArtistColumns.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val cursor = context.contentResolver.query(internalContentUri, projection, null, null, null)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val path = cursor.getString(0)
                val album = cursor.getString(1)
                val artist = cursor.getString(2)
                val image = cursor.getLong(3).toString()

                val uri = Uri.parse("content://media/external/audio/albumart")
                val songUri = Uri.withAppendedPath(uri, image).toString()

                val name = path.substring(path.lastIndexOf("/") + 1)
                val audioFile = AudioModel(path, name, album, artist, songUri)
                tempAudioList.add(audioFile)
            }
            cursor.close()
        }
        allAudio.value = tempAudioList
        return allAudio
    }

    fun filterAudio(search: String?, allAudioList: ArrayList<AudioModel>): ArrayList<AudioModel> {
        val filteredList: ArrayList<AudioModel> = ArrayList()
        for (item in allAudioList) {
            if (item.audioName.toLowerCase().contains(search!!.toLowerCase())) {
                filteredList.add(item)
            }
        }
        return filteredList
    }
}