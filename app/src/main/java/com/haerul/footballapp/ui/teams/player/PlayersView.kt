package com.haerul.footballapp.ui.teams.player

import com.haerul.footballapp.model.Player

interface PlayersView {
    fun showLoading()
    fun hideLoading()
    fun showPlayers(data: List<Player>)
    fun errorLoading(e: String?)
}