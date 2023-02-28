package com.example.musicbajao.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicbajao.databinding.FragmentDownloadsBinding
import com.example.musicbajao.model.AudioModel
import com.example.musicbajao.view.adapters.MusicListAdapter
import com.example.musicbajao.viewModel.home.DownloadsViewModel

class DownloadsFragment : Fragment() {

    private lateinit var binding: FragmentDownloadsBinding
    private lateinit var musicListAdapter: MusicListAdapter
    private lateinit var viewModel: DownloadsViewModel
    private lateinit var contextDownloads: Context
    private var allAudio = ArrayList<AudioModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDownloadsBinding.inflate(inflater, container, false)
        binding.recyclerAudioList.layoutManager = LinearLayoutManager(contextDownloads)
        viewModel = ViewModelProvider(this).get(DownloadsViewModel::class.java)

        try {
            viewModel.getAllAudioFromDevice(contextDownloads).observe(viewLifecycleOwner, Observer { audioList ->
                allAudio = audioList
                musicListAdapter = MusicListAdapter(contextDownloads, allAudio)
                binding.recyclerAudioList.adapter = musicListAdapter
            })
            addSearchView()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    private fun addSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = viewModel.filterAudio(newText, allAudio)
                musicListAdapter = MusicListAdapter(contextDownloads, filteredList)
                binding.recyclerAudioList.adapter = musicListAdapter
                return false
            }

        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextDownloads = context
    }
}


