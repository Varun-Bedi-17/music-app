package com.example.musicbajao.view.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicbajao.R
import com.example.musicbajao.databinding.RecyclerAudioCardBinding
import com.example.musicbajao.model.AudioModel
import com.example.musicbajao.view.activity.MusicPlayerActivity

class MusicListAdapter(val context: Context, private var audioList: ArrayList<AudioModel>) :
    RecyclerView.Adapter<MusicListAdapter.MusicListViewHolder>() {

    private lateinit var binding: RecyclerAudioCardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicListViewHolder {

        // Binding
        binding =
            RecyclerAudioCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicListViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MusicListViewHolder, position: Int) {
            binding.songName.text = audioList[position].audioName
            binding.songArtist.text = audioList[position].audioArtist

//            binding.songIcon.setImageURI(Uri.parse(audioList[position].audioImage))

            // To set image
            Glide.with(context).load(audioList[position].audioImage)
                .apply(RequestOptions.placeholderOf(R.drawable.music_logo)).into(binding.songIcon)

        binding.cardView.setOnClickListener {
            val intent = Intent(context, MusicPlayerActivity::class.java)
            intent.putExtra("audio", audioList[position])
            intent.putExtra("audiolist", audioList)
            intent.putExtra("position", position)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return audioList.size
    }


    // Items not repeat
    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position

    class MusicListViewHolder(binding: RecyclerAudioCardBinding) :
        RecyclerView.ViewHolder(binding.root)
}