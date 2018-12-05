package com.haerul.footballapp.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.view.View
import com.haerul.footballapp.R
import kotlinx.android.synthetic.main.activity_home.*

open class FragmentTransaction {
    companion object {
        fun pushFragments(activity: FragmentActivity?, tag: String, fragment: Fragment) {

            val manager = activity!!.supportFragmentManager
            val ft = manager.beginTransaction()

            if (manager.findFragmentByTag(tag) == null) {
                ft.add(R.id.main_container, fragment, tag)
            }

            val fragmentMatch = manager.findFragmentByTag(KeyValue.TAG_MATCH)
            val fragmentTeam = manager.findFragmentByTag(KeyValue.TAG_TEAM)
            val fragmentFavorite = manager.findFragmentByTag(KeyValue.TAG_FAVORITE)
            val fragmentMatchSearch = manager.findFragmentByTag(KeyValue.TAG_MATCH_SEARCH)
            val fragmentTeamSearch = manager.findFragmentByTag(KeyValue.TAG_TEAM_SEARCH)

            if (fragmentMatch != null) {
                ft.hide(fragmentMatch)
            }
            if (fragmentTeam != null) {
                ft.hide(fragmentTeam)
            }
            if (fragmentFavorite != null) {
                ft.remove(fragmentFavorite)
            }
            if (fragmentMatchSearch != null) {
                ft.remove(fragmentMatchSearch)
                activity.bottom_navigation.visibility = View.VISIBLE
            }
            if (fragmentTeamSearch != null) {
                ft.remove(fragmentTeamSearch)
                activity.bottom_navigation.visibility = View.VISIBLE
            }

            when(tag) {
                KeyValue.TAG_MATCH -> {
                    if (fragmentMatch != null) {
                        ft.show(fragmentMatch)
                    }
                }
                KeyValue.TAG_TEAM -> {
                    if (fragmentTeam != null) {
                        ft.show(fragmentTeam)
                    }
                }
                KeyValue.TAG_FAVORITE -> {
                    if (fragmentFavorite != null) {
                        ft.show(fragmentFavorite)
                    }
                }
                KeyValue.TAG_MATCH_SEARCH -> {
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    activity.bottom_navigation.visibility = View.GONE
                }
                KeyValue.TAG_TEAM_SEARCH -> {
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    activity.bottom_navigation.visibility = View.GONE
                }
            }

            ft.commitAllowingStateLoss()
        }
    }
}
