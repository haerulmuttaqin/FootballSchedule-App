package com.haerul.footballapp.ui.match.detail

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.google.gson.Gson
import com.haerul.footballapp.api.ApiRepository
import com.haerul.footballapp.api.TheSportDBApi
import com.haerul.footballapp.helper.database
import com.haerul.footballapp.model.Match
import com.haerul.footballapp.model.TeamResponse
import com.haerul.footballapp.util.CoroutineContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MatchDetailPresenter(private val view: MatchDetailView,
                           private val apiRepository: ApiRepository,
                           private val gson: Gson,
                           private val coroutinesCtx: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getMatches(idHomeTeam: String?, idAwayTeam: String?) = GlobalScope.launch(coroutinesCtx.main) {
        view.showLoading()

        val homeTeam = makeRequest( TheSportDBApi.getTeamDetail(idHomeTeam) )
        val awayTeam = makeRequest( TheSportDBApi.getTeamDetail(idAwayTeam) )

        try {
            view.showTeam(homeTeam.await().teams, awayTeam.await().teams)
        } catch (e: Throwable) {
            view.errorLoading(e.localizedMessage)
        }

        view.hideLoading()
    }

    private fun makeRequest(url: String) = GlobalScope.async(coroutinesCtx.io) {
        gson.fromJson(apiRepository.doRequest(url),
                TeamResponse::class.java)

    }

    fun getFavoriteState(context: Context, idMatch: String) : Boolean {

        var favorite = false

        try {
            context.database.use {
                val result = select(Match.TABLE_MATCHES)
                        .whereArgs("(ID_EVENT = {id})",
                                "id" to idMatch)
                        .parseList(classParser<Match>())
                favorite = !result.isEmpty()

            }
        } catch (e: SQLiteConstraintException)  {
            e.printStackTrace()
        }

        return favorite
    }

    fun removeFromFavorite(context: Context, idMatch: String) {

        doAsync {

            val result = try {

                context.database.use {
                    delete(Match.TABLE_MATCHES, "(ID_EVENT = {id})",
                            "id" to idMatch)
                }

                "Removed to favorite"

                } catch (e: SQLiteConstraintException){
                    e.localizedMessage
                }

            uiThread {
                view.onRemoveFromFavorite(result)
            }
        }
    }

    fun addToFavorite(context: Context, data: Match) {

        doAsync {

            val result = try {

                context.database.use {
                    insert(Match.TABLE_MATCHES,
                            Match.ID_EVENT to data.idEvent,
                            Match.DATE to data.dateEvent,
                            Match.TIME to data.strTime,
                            Match.HOME_ID to data.idHomeTeam,
                            Match.HOME_TEAM to data.strHomeTeam,
                            Match.HOME_SCORE to data.intHomeScore,
                            Match.HOME_FORMATION to data.strHomeFormation,
                            Match.HOME_GOAL_DETAILS to data.strHomeGoalDetails,
                            Match.HOME_SHOTS to data.intHomeShots,
                            Match.HOME_LINEUP_GOALKEEPER to data.strHomeLineupGoalkeeper,
                            Match.HOME_LINEUP_DEFENSE to data.strHomeLineupDefense,
                            Match.HOME_LINEUP_MIDFIELD to data.strHomeLineupMidfield,
                            Match.HOME_LINEUP_FORWARD to data.strHomeLineupForward,
                            Match.HOME_LINEUP_SUBSTITUTES to data.strHomeLineupSubstitutes,
                            Match.AWAY_ID to data.idAwayTeam,
                            Match.AWAY_TEAM to data.strAwayTeam,
                            Match.AWAY_SCORE to data.intAwayScore,
                            Match.AWAY_FORMATION to data.strAwayFormation,
                            Match.AWAY_GOAL_DETAILS to data.strAwayGoalDetails,
                            Match.AWAY_SHOTS to data.intAwayShots,
                            Match.AWAY_LINEUP_GOALKEEPER to data.strAwayLineupGoalkeeper,
                            Match.AWAY_LINEUP_DEFENSE to data.strAwayLineupDefense,
                            Match.AWAY_LINEUP_MIDFIELD to data.strAwayLineupMidfield,
                            Match.AWAY_LINEUP_FORWARD to data.strAwayLineupForward,
                            Match.AWAY_LINEUP_SUBSTITUTES to data.strAwayLineupSubstitutes)
                }

                "Added to favorite"

            } catch (e: SQLiteConstraintException){
                e.localizedMessage
            }

            uiThread {
                view.onAddToFavorite(result)
            }
        }
    }
}