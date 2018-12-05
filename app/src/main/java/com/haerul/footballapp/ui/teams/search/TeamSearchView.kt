package com.haerul.footballapp.ui.teams.search

import android.view.Menu
import android.view.MenuInflater
import com.haerul.footballapp.model.Team

interface TeamSearchView {
    fun showLoading()
    fun hideLoading()
    fun showSearchResult(data: List<Team>)
    fun errorLoading(e: String?)
    fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?)
}