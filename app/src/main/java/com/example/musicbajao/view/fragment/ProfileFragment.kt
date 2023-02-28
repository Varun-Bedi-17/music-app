package com.example.musicbajao.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.musicbajao.databinding.FragmentBottomSheetBinding
import com.example.musicbajao.databinding.FragmentProfileBinding
import com.example.musicbajao.view.activity.REQUEST_CODE_CAMERA
import com.example.musicbajao.view.activity.REQUEST_CODE_WRITE_STORAGE
import com.example.musicbajao.view.activity.LoginScreen
import com.example.musicbajao.viewModel.loginSinup.home.MyProfileViewModel
import com.example.musicbajao.viewModel.loginSinup.home.MyProfileViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var bindingForBottomSheet: FragmentBottomSheetBinding? = null
    private lateinit var viewModel: MyProfileViewModel
    private lateinit var contextI: Context


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val preferenceForLogin =
            requireActivity().getSharedPreferences("isLogin", AppCompatActivity.MODE_PRIVATE)
        val string = preferenceForLogin.getString("userId", "default")


        viewModel = ViewModelProvider(
            this,
            MyProfileViewModelFactory(contextI)
        ).get(MyProfileViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        addClickListener()
        viewModel.readLoginDataFromViewModel(string!!)
        viewModel.addImageFromDatabase(binding.circularProfile)

        return binding.root
    }

    private fun addClickListener() {
        binding.signOutBtn.setOnClickListener {
            val preferenceForLogout =
                requireContext().getSharedPreferences("isLogin", AppCompatActivity.MODE_PRIVATE)
            val preferenceForLogoutEdit = preferenceForLogout.edit()

            preferenceForLogoutEdit.putBoolean("flag", false)
            preferenceForLogoutEdit.apply()

            startActivity(Intent(context, LoginScreen::class.java))
            activity?.finish()
        }

        var flag = 0
        binding.editProfile.setOnClickListener {
            if (flag == 0) {
                binding.inputName.isEnabled = true
                binding.inputPassword.isEnabled = true
                binding.editProfile.text = "Done"
                binding.signOutBtn.isEnabled = false
                binding.dataChanged.visibility = View.INVISIBLE
                binding.inputPassword.isVisible = true
                binding.password.isVisible = true
                binding.editProfileButton.isVisible = true
                binding.firstname.isVisible = true
                binding.inputName.isVisible = true
                flag = 1
            } else {

                viewModel.updateData()
                binding.name.text = viewModel.welcomeQuote.value.toString()
                binding.inputName.isEnabled = false
                binding.inputPassword.isEnabled = false
                binding.editProfile.text = "Update Profile"
                binding.dataChanged.isVisible = true
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        binding.dataChanged.visibility = View.INVISIBLE
                    },
                    1000
                )
                binding.signOutBtn.isEnabled = true
                binding.inputPassword.visibility = View.INVISIBLE
                binding.password.visibility = View.INVISIBLE
                binding.editProfileButton.visibility = View.INVISIBLE
                binding.firstname.visibility = View.INVISIBLE
                binding.inputName.visibility = View.INVISIBLE

                flag = 0
            }
        }

        binding.editProfileButton.setOnClickListener {

            // For alert dialog.

//            val builder = AlertDialog.Builder(contextI)
//            builder.setMessage("Choose picture")
//                .setPositiveButton("Gallery",
//                    DialogInterface.OnClickListener { dialog, id ->
//                        openGalleryForImage()
//                    })
//                .setNegativeButton("Camera",
//                    DialogInterface.OnClickListener { dialog, id ->
//                        openCamera()
//                    })
//            // Create the AlertDialog object and return it
//            builder.create().show()


            // Bottom Sheet dialog
            val dialog = BottomSheetDialog(contextI)
            bindingForBottomSheet = FragmentBottomSheetBinding.inflate(layoutInflater)

            dialog.setContentView(bindingForBottomSheet!!.root)

            bindingForBottomSheet!!.cameraButton.setOnClickListener {
                openCamera()
                dialog.dismiss()
            }
            bindingForBottomSheet!!.galleryButton.setOnClickListener {
                openGalleryForImage()
                dialog.dismiss()
            }
            dialog.show()

        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(
            intent,
            REQUEST_CODE_CAMERA
        )                  // Request codes are declared in home activity.
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_WRITE_STORAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_WRITE_STORAGE) {

                // data as intent
                binding.circularProfile.setImageURI(data.data)         // handle chosen image
                viewModel.inputProfileImage.value = data.data
            }

            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {

                // data as bundle
                // bundle to bitmap
                val bitmapImage = data.extras?.get("data") as Bitmap
                binding.circularProfile.setImageBitmap(bitmapImage)

                // Convert bitmap to string.
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream())
                val path = MediaStore.Images.Media.insertImage(
                    contextI.contentResolver,
                    bitmapImage,
                    "image",
                    "description"
                )
                val uri = Uri.parse(path)

                viewModel.inputProfileImage.value = uri

            }
        }
    }

    override fun onAttach(context: Context) {
        contextI = context
        super.onAttach(context)
    }


}