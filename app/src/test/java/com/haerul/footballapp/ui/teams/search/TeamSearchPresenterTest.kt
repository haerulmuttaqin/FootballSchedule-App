package com.haerul.footballapp.ui.teams.search

import com.google.gson.Gson
import com.haerul.footballapp.api.ApiRepository
import com.haerul.footballapp.api.TheSportDBApi
import com.haerul.footballapp.model.Match
import com.haerul.footballapp.model.MatchSearchResponse
import com.haerul.footballapp.model.Team
import com.haerul.footballapp.model.TeamResponse
import com.haerul.footballapp.util.TestContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class TeamSearchPresenterTest {

    private lateinit var presenter: TeamSearchPresenter

    @Mock
    private
    lateinit var view: TeamSearchView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = TeamSearchPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetSearchMatches() {
        val team: MutableList<Team> = mutableListOf()
        val response = TeamResponse(team)
        val keyword = "Arsenal"

        GlobalScope.launch {
            Mockito.`when`(gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getTeamSearch(keyword)),
                    TeamResponse::class.java
            )).thenReturn(response)

            presenter.getSearchTeam(keyword)

            Mockito.verify(view).showLoading()
            try {
                Mockito.verify(view).showSearchResult(team)
            } catch (e: Exception) {
                Mockito.verify(view).errorLoading(e.localizedMessage)
            }
            Mockito.verify(view).hideLoading()
        }
    }
}