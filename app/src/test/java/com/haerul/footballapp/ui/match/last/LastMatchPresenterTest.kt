package com.haerul.footballapp.ui.match.last

import com.google.gson.Gson
import com.haerul.footballapp.api.ApiRepository
import com.haerul.footballapp.api.TheSportDBApi
import com.haerul.footballapp.model.Match
import com.haerul.footballapp.model.MatchResponse
import com.haerul.footballapp.util.TestContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test

import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class LastMatchPresenterTest {

    private lateinit var presenter: LastMatchPresenter

    @Mock
    private
    lateinit var view: LastMatchView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = LastMatchPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetLastMatches() {
        val match: MutableList<Match> = mutableListOf()
        val response = MatchResponse(match)
        val leagueId = "4328"

        GlobalScope.launch {
            `when`(gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getLastMatch(leagueId)),
                    MatchResponse::class.java
            )).thenReturn(response)

            presenter.getMatches(leagueId)

            verify(view).showLoading()
            try {
                verify(view).showMatches(match)
            } catch (e: Exception) {
                verify(view).errorLoading(e.localizedMessage)
            }
            verify(view).hideLoading()
        }
    }
}