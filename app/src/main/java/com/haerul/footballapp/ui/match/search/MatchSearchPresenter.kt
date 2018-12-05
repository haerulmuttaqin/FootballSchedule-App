package com.haerul.footballapp.ui.match.search

import com.google.gson.Gson
import com.haerul.footballapp.api.ApiRepository
import com.haerul.footballapp.api.TheSportDBApi
import com.haerul.footballapp.model.MatchSearchResponse
import com.haerul.footballapp.util.CoroutineContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MatchSearchPresenter(private val view: MatchSearchView,
                         private val apiRepository: ApiRepository,
                         private val gson: Gson,
                         private val coroutinesCtx: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getSearchMatches(key: String?) = GlobalScope.launch(coroutinesCtx.main) {
        view.showLoading()

        if (!key?.trim().isNullOrEmpty()){
            val data = makeRequest( TheSportDBApi.getMatchSearch(key) )

            try {
                view.showSearchResult(data.await().events)
            } catch (e: Throwable) {
                view.errorLoading(e.localizedMessage)
            }
        }

        view.hideLoading()
    }

    private fun makeRequest(url: String) = GlobalScope.async(coroutinesCtx.io) {
        gson.fromJson(apiRepository.doRequest(url),
                MatchSearchResponse::class.java)
    }
}