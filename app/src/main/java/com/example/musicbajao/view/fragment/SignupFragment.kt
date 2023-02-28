package com.example.musicbajao.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.example.musicbajao.R
import com.example.musicbajao.databinding.FragmentSignupBinding
import com.example.musicbajao.viewModel.loginSinup.SignupViewModel
import com.example.musicbajao.viewModel.loginSinup.SignupViewModelFactory

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: SignupViewModel
    private lateinit var contextI: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(
            this,
            SignupViewModelFactory(contextI)
        ).get(SignupViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.userAlreadyAvailable.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.inputUsername.error = "User already exists"
            }
        }
        viewModel.inValidPassword.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.inputPassword.error = "Password does not match"
            }
        }
        viewModel.navigateToSignupsSplashScreen.observe(viewLifecycleOwner) {
            if (it == true) {
                // For removing keyboard.
                val view: View? = activity?.currentFocus

                if (view != null) {
                    val inputMethodManager =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                }

                binding.inputUsername.error = null
                binding.inputName.error = null
                binding.inputPassword.error = null
                binding.confirmInputPassword.error = null
                val fragment = WelcomeSignupSplash()
                val fragManager = requireActivity().supportFragmentManager
                fragManager.popBackStackImmediate()
                fragManager.beginTransaction().replace(R.id.frameLayout, fragment)
                    .addToBackStack(null).commit()
            }
        }
        viewModel.emptyFirstname.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.inputName.error = "Please enter this field"
            }
        }
        viewModel.emptyUsername.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.inputUsername.error = "Please enter this field"
            }
        }
        viewModel.emptyPassword.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.inputPassword.error = "Please enter this field"

            }
        }
        viewModel.emptyConfirmPass.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.inputPassword.error = "Please enter this field"

            }
        }
        attachListeners()

        return binding.root
    }

    private fun attachListeners() {
        binding.signupButton.setOnClickListener {
            val isValidate =
                viewModel.readLoginDataFromViewModel(binding.inputUsername.text.toString())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contextI = context
    }


}