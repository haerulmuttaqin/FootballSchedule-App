package com.haerul.footballapp.ui.match.search

import android.view.Menu
import android.view.MenuInflater
import com.haerul.footballapp.model.Match

interface MatchSearchView {
    fun showLoading()
    fun hideLoading()
    fun showSearchResult(data: List<Match>)
    fun errorLoading(e: String?)
    fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?)
}