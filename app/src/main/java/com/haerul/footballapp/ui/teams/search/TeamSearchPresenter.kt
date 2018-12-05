package com.haerul.footballapp.ui.teams.search

import com.google.gson.Gson
import com.haerul.footballapp.api.ApiRepository
import com.haerul.footballapp.api.TheSportDBApi
import com.haerul.footballapp.model.TeamResponse
import com.haerul.footballapp.util.CoroutineContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TeamSearchPresenter(private val view: TeamSearchView,
                          private val apiRepository: ApiRepository,
                          private val gson: Gson,
                          private val coroutinesCtx: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getSearchTeam(key: String?) = GlobalScope.launch(coroutinesCtx.main) {
        view.showLoading()

        if (!key.isNullOrEmpty()){
            val data = makeRequest( TheSportDBApi.getTeamSearch(key) )

            try {
                view.showSearchResult(data.await().teams)
            } catch (e: Throwable) {
                view.errorLoading(e.localizedMessage)
            }
        }

        view.hideLoading()
    }

    private fun makeRequest(url: String) = GlobalScope.async(coroutinesCtx.io) {
        gson.fromJson(apiRepository.doRequest(url),
                TeamResponse::class.java)
    }
}