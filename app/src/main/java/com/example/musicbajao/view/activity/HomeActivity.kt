package com.example.musicbajao.view.activity

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.musicbajao.R
import com.example.musicbajao.databinding.ActivityHomeBinding
import com.example.musicbajao.view.adapters.HomeViewPagerAdapter
import com.example.musicbajao.view.fragment.DownloadsFragment
import com.example.musicbajao.view.fragment.HomeFragment
import com.example.musicbajao.view.fragment.ProfileFragment

const val REQUEST_CODE_STORAGE = 103
const val REQUEST_CODE_WRITE_STORAGE = 110
const val REQUEST_CODE_CAMERA = 100
const val REQUEST_CODE_ALL = 105

class HomeActivity : AppCompatActivity() {
    companion object {
        private lateinit var binding: ActivityHomeBinding
        private lateinit var adapterViewPager: HomeViewPagerAdapter
        private val arrayOfUngrantedPermissions = ArrayList<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setUpTabs()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission(android.Manifest.permission.CAMERA, REQUEST_CODE_CAMERA)
            checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_STORAGE)
            checkPermission(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                REQUEST_CODE_WRITE_STORAGE
            )
            addUngrantedPermissions(arrayOfUngrantedPermissions, REQUEST_CODE_ALL)
        }

    }


    private fun setUpTabs() {
        adapterViewPager = HomeViewPagerAdapter(supportFragmentManager)
        adapterViewPager.addFragment(HomeFragment(), "Home")
        adapterViewPager.addFragment(DownloadsFragment(), "Downloads")
        adapterViewPager.addFragment(ProfileFragment(), "Profile")

        binding.viewPager.adapter = adapterViewPager
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_home_foreground)
        binding.tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_downloads_icon_foreground)
        binding.tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_my_profile_foreground)
    }

    override fun onBackPressed() {
        if (binding.viewPager.currentItem == 0) {
            moveTaskToBack(true)
        } else {
            binding.viewPager.currentItem = 0
        }
    }


    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            arrayOfUngrantedPermissions.add(permission)
        } else {
//            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addUngrantedPermissions(
        arrayOfUngrantedPermissions: ArrayList<String>,
        requestCode: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && arrayOfUngrantedPermissions.isNotEmpty()) {
//            Toast.makeText(this, "Requesting Permission", Toast.LENGTH_SHORT).show()
            requestPermissions(arrayOfUngrantedPermissions.toTypedArray(), requestCode)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == REQUEST_CODE_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == REQUEST_CODE_WRITE_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Galley Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(this, "Gallery Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        MusicPlayerActivity.musicService?.mediaPlayer?.stop()
        MusicPlayerActivity.musicService?.stopForeground(true)
    }


}