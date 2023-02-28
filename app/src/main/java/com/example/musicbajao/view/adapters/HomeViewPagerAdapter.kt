package com.example.musicbajao.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class HomeViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitle = ArrayList<String?>()

    // Return number of fragments we needed in tab bar.
    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }


    fun addFragment(fragment: Fragment, title: String?){
        mFragmentList.add(fragment)
        mFragmentTitle.add(title)
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitle[position]
    }
}