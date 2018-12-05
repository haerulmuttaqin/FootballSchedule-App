package com.haerul.footballapp.ui.favorite.match

import com.haerul.footballapp.model.Match

interface FavoriteMatchView {
    fun showLoading()
    fun hideLoading()
    fun showFavorite(favorites: List<Match>)
    fun errorLoading(e: String?)
}