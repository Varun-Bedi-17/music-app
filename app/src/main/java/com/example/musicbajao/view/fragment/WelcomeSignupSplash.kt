package com.example.musicbajao.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.musicbajao.databinding.FragmentWelcomeSignupSplashBinding
import com.example.musicbajao.view.activity.LoginScreen


class WelcomeSignupSplash : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = FragmentWelcomeSignupSplashBinding.inflate(inflater, container, false)


        binding.nxtButton.setOnClickListener {
            Intent(requireActivity(), LoginScreen::class.java).also {
                activity?.finish()
                startActivity(it)
            }
        }

        return binding.root

    }


}