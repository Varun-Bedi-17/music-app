package com.example.musicbajao.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.example.musicbajao.*
import com.example.musicbajao.databinding.SplashWelcomeBinding

class WelcomeSplash : AppCompatActivity() {
    private lateinit var binding: SplashWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SplashWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim)
        binding.imageLogo.startAnimation(anim)


        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.title.text = "Play your favourite songs !!"

            },
            2000
        )

        // Handler().postDelayed()
        Handler(Looper.getMainLooper()).postDelayed(
            {
                //Using shared preference for login
                val preference = getSharedPreferences("isLogin", MODE_PRIVATE)
                val isLogin = preference.getBoolean("flag", false)
                val intentForScreen = if (isLogin) {
                    Intent(this, HomeActivity::class.java)
                    }
                else{
                    Intent(this, LoginScreen::class.java)
                }
                startActivity(intentForScreen)
                finish()
            },
            3000
        )



    }
}