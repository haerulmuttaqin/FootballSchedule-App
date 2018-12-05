package com.haerul.footballapp.ui.match.next

import com.haerul.footballapp.model.LeagueResponse
import com.haerul.footballapp.model.Match

interface NextMatchView {
    fun showLoading()
    fun hideLoading()
    fun showLeagues(data: LeagueResponse)
    fun showMatches(data: List<Match>)
    fun errorLoading(e: String?)
}