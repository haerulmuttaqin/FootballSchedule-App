package com.haerul.footballapp.ui.teams

import com.haerul.footballapp.model.LeagueResponse
import com.haerul.footballapp.model.Team

interface TeamView {
    fun showLoading()
    fun hideLoading()
    fun showLeagues(data: LeagueResponse)
    fun showTeams(data: List<Team>)
    fun errorLoading(e: String?)
}