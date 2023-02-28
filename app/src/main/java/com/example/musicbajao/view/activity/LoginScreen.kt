package com.example.musicbajao.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.musicbajao.R
import com.example.musicbajao.databinding.LoginScreenBinding
import com.example.musicbajao.view.fragment.SignupFragment
import com.example.musicbajao.viewModel.loginSinup.LoginViewModel
import com.example.musicbajao.viewModel.loginSinup.LoginViewModelFactory

class LoginScreen : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: LoginScreenBinding
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.login_screen)
        viewModel =
            ViewModelProvider(this, LoginViewModelFactory(this)).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this



        viewModel.userNotAvailable.observe(this) {
            if (it == true) {
                binding.inputUsername.error = "User not exists. Please signup"
                binding.inputUsername.isEnabled = true
            }
        }
        viewModel.inValidPassword.observe(this) {
            if (it == true) {
                binding.inputPassword.error = "Wrong Password"
                binding.inputPassword.isEnabled = true
            }
        }
        viewModel.navigateToHomeScreen.observe(this) {
            if (it == true) {
                addActivity()
            }
        }
        viewModel.emptyUsername.observe(this) {
            if (it == true) {
                binding.inputUsername.error = "Please enter this field"
                binding.inputUsername.isEnabled = true
            }
        }
        viewModel.emptyPassword.observe(this) {
            if (it == true) {
                binding.inputPassword.error = "Please enter this field"
                binding.inputPassword.isEnabled = true
            }
        }
        attachListeners()


    }

    private fun attachListeners() {
        binding.signup.setOnClickListener(this)

        binding.loginBtn.setOnClickListener(this)
    }

    private fun validateEmptyForm() {
        val isValidate = viewModel.readLoginDataFromViewModel(binding.inputUsername.text.toString())

    }


    private fun addActivity() {
        // For removing keyboard.
        val view: View? = this.currentFocus

        if (view != null) {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // Shared Preference
        val preferenceForLogin = getSharedPreferences("isLogin", MODE_PRIVATE)
        val preferenceForLoginEdit = preferenceForLogin.edit()

        preferenceForLoginEdit.putBoolean("flag", true)
        preferenceForLoginEdit.apply()


        Intent(this, HomeActivity::class.java).also {
            finish()
            startActivity(it)
        }

    }


    private fun addFragment(fragment: Fragment) {
        binding.inputUsername.error = null
        binding.inputPassword.error = null

        // For removing keyboard.
        val view: View? = this.currentFocus

        if (view != null) {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            addToBackStack("login")
            commit()
        }
    }

    override fun onClick(button: View?) {
        when (button) {
            binding.signup -> addFragment(SignupFragment())
            binding.loginBtn -> validateEmptyForm()

        }
    }
}
