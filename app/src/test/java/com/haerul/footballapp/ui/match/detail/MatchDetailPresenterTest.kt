package com.haerul.footballapp.ui.match.detail

import com.google.gson.Gson
import com.haerul.footballapp.api.ApiRepository
import com.haerul.footballapp.api.TheSportDBApi
import com.haerul.footballapp.model.Team
import com.haerul.footballapp.model.TeamResponse
import com.haerul.footballapp.util.TestContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class MatchDetailPresenterTest {

    private lateinit var presenter: MatchDetailPresenter

    @Mock
    private
    lateinit var view: MatchDetailView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MatchDetailPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetDetailMatches() {
        val team: MutableList<Team> = mutableListOf()
        val response = TeamResponse(team)
        val homeTeam = "133932"
        val awayTeam = "133600"

        GlobalScope.launch {
            `when`(gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getTeamDetail(homeTeam)),
                    TeamResponse::class.java
            )).thenReturn(response)

            `when`(gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getTeamDetail(awayTeam)),
                    TeamResponse::class.java
            )).thenReturn(response)

            presenter.getMatches(homeTeam, awayTeam)

            verify(view).showLoading()
            try {
                verify(view).showTeam(team, team)
            } catch (e: Exception) {
                verify(view).errorLoading(e.localizedMessage)
            }
            verify(view).hideLoading()
        }
    }

    @Test
    fun testGetDetailMatches_error_loading_badge() {
        val team: MutableList<Team> = mutableListOf()
        val response = TeamResponse(team)
        val homeTeam = "error"
        val awayTeam = "error"

        GlobalScope.launch {
            `when`(gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getTeamDetail(homeTeam)),
                    TeamResponse::class.java
            )).thenReturn(response)

            `when`(gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getTeamDetail(awayTeam)),
                    TeamResponse::class.java
            )).thenReturn(response)

            presenter.getMatches(homeTeam, awayTeam)

            verify(view).showLoading()
            try {
                verify(view).showTeam(team, team)
            } catch (e: Exception) {
                verify(view).errorLoading(e.localizedMessage)
            }
            verify(view).hideLoading()
        }
    }
}