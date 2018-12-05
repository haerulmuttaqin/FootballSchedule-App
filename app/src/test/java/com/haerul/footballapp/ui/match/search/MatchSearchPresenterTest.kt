package com.haerul.footballapp.ui.match.search

import com.google.gson.Gson
import com.haerul.footballapp.api.ApiRepository
import com.haerul.footballapp.api.TheSportDBApi
import com.haerul.footballapp.model.Match
import com.haerul.footballapp.model.MatchSearchResponse
import com.haerul.footballapp.util.TestContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MatchSearchPresenterTest {

    private lateinit var presenter: MatchSearchPresenter

    @Mock
    private
    lateinit var view: MatchSearchView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MatchSearchPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetSearchMatches() {
        val match: MutableList<Match> = mutableListOf()
        val response = MatchSearchResponse(match)
        val keyword = "Barcelona"

        GlobalScope.launch {
            Mockito.`when`(gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getNextMatch(keyword)),
                    MatchSearchResponse::class.java
            )).thenReturn(response)

            presenter.getSearchMatches(keyword)

            Mockito.verify(view).showLoading()
            try {
                Mockito.verify(view).showSearchResult(match)
            } catch (e: Exception) {
                Mockito.verify(view).errorLoading(e.localizedMessage)
            }
            Mockito.verify(view).hideLoading()
        }
    }
}