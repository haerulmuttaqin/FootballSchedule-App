package com.haerul.footballapp.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager, private val map: Map<String, Fragment>): FragmentPagerAdapter(fm){

    private val pages = map.values.toList()
    private val titles = map.keys.toList()

    override fun getItem(position: Int): Fragment = pages[position]

    override fun getCount(): Int = map.size

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> titles[0]
            else -> titles[1]
        }
    }
}
