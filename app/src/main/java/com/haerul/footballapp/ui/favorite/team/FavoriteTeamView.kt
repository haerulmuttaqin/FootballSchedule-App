package com.haerul.footballapp.ui.favorite.team

import com.haerul.footballapp.model.Team

interface FavoriteTeamView {
    fun showLoading()
    fun hideLoading()
    fun showFavorite(favorites: List<Team>)
    fun errorLoading(e: String?)
}