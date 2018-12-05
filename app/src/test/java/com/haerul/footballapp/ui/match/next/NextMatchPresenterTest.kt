package com.haerul.footballapp.ui.match.next

import com.google.gson.Gson
import com.haerul.footballapp.api.ApiRepository
import com.haerul.footballapp.api.TheSportDBApi
import com.haerul.footballapp.model.Match
import com.haerul.footballapp.model.MatchResponse
import com.haerul.footballapp.util.TestContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class NextMatchPresenterTest {

    private lateinit var presenter: NextMatchPresenter

    @Mock
    private
    lateinit var view: NextMatchView

    @Mock
    private
    lateinit var gson: Gson

    @Mock
    private
    lateinit var apiRepository: ApiRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = NextMatchPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetNextMatches() {
        val match: MutableList<Match> = mutableListOf()
        val response = MatchResponse(match)
        val leagueId = "4328"

        GlobalScope.launch {
            `when`(gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getNextMatch(leagueId)),
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