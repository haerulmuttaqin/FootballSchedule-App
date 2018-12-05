package com.haerul.footballapp.ui.match.detail

import com.haerul.footballapp.model.Team

interface MatchDetailView {
    fun showLoading()
    fun hideLoading()
    fun showTeam(homeTeam: List<Team>, awayTeam: List<Team>)
    fun errorLoading(t: String?)
    fun onRemoveFromFavorite(result: String?)
    fun onAddToFavorite(result: String?)
}