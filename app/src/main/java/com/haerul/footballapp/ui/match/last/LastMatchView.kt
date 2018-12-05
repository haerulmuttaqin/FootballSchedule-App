package com.haerul.footballapp.ui.match.last

import com.haerul.footballapp.model.LeagueResponse
import com.haerul.footballapp.model.Match

interface LastMatchView {
    fun showLoading()
    fun hideLoading()
    fun showLeagues(data: LeagueResponse)
    fun showMatches(data: List<Match>)
    fun errorLoading(e: String?)
}