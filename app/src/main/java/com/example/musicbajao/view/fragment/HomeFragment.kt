package com.example.musicbajao.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicbajao.databinding.FragmentHomeBinding
import com.example.musicbajao.model.repository.HomeRepo
import com.example.musicbajao.view.adapters.MusicListApiAdapter
import com.example.musicbajao.viewModel.home.HomeViewModel
import com.example.musicbajao.viewModel.loginSinup.home.HomeViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var contextFragment: Context
    private lateinit var viewModel: HomeViewModel
    private lateinit var musicListApiAdapter: MusicListApiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.recyclerAudioList.layoutManager = LinearLayoutManager(contextFragment)

        viewModel = ViewModelProvider(this, HomeViewModelFactory(HomeRepo(contextFragment))).get(
            HomeViewModel::class.java
        )

        musicListApiAdapter = MusicListApiAdapter(contextFragment, emptyList())
        binding.recyclerAudioList.adapter = musicListApiAdapter


        GlobalScope.launch(Dispatchers.IO) {
            val allSongs = viewModel.getAllSongs()
            withContext(Dispatchers.Main) {
                if (allSongs.isNotEmpty()) {
                    val musicListApiAdapter = MusicListApiAdapter(contextFragment, allSongs)
                    binding.recyclerAudioList.adapter = musicListApiAdapter
                    binding.noInteretConnection.visibility = View.GONE

                } else {
                    binding.noInteretConnection.visibility = View.VISIBLE
                }
            }
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextFragment = context
    }
}





