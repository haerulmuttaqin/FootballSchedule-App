package com.haerul.footballapp.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.haerul.footballapp.R
import com.haerul.footballapp.R.layout.activity_home
import com.haerul.footballapp.ui.favorite.FavoriteFragment
import com.haerul.footballapp.ui.match.MatchFragment
import com.haerul.footballapp.ui.teams.TeamFragment
import com.haerul.footballapp.util.FragmentTransaction.Companion.pushFragments
import com.haerul.footballapp.util.KeyValue.Companion.TAG_FAVORITE
import com.haerul.footballapp.util.KeyValue.Companion.TAG_MATCH
import com.haerul.footballapp.util.KeyValue.Companion.TAG_TEAM
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_home)

        pushFragments(this, TAG_MATCH, MatchFragment())
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                bottom_navigation.selectedItemId -> return@setOnNavigationItemSelectedListener false
                R.id.matches -> {
                    pushFragments(this, TAG_MATCH, MatchFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.teams -> {
                    pushFragments(this, TAG_TEAM, TeamFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.favorites -> {
                    pushFragments(this, TAG_FAVORITE, FavoriteFragment())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }

        bottom_navigation.selectedItemId = R.id.matches
    }
}