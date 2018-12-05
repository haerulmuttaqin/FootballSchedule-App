package com.haerul.footballapp.ui.teams.detail

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.haerul.footballapp.helper.database
import com.haerul.footballapp.model.Team
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class TeamDetailPresenter(private val view: TeamDetailView) {

    fun getFavoriteState(context: Context, idTeam: String) : Boolean {

        var favorite = false

        try {
            context.database.use {
                val result = select(Team.TABLE_TEAMS)
                        .whereArgs("(ID_TEAM = {id})",
                                "id" to idTeam)
                        .parseList(classParser<Team>())
                favorite = !result.isEmpty()

            }
        } catch (e: SQLiteConstraintException)  {
            e.printStackTrace()
        }

        return favorite
    }

    fun removeFromFavorite(context: Context, idTeam: String) {

        doAsync {

            val result = try {

                context.database.use {
                    delete(Team.TABLE_TEAMS, "(ID_TEAM = {id})",
                            "id" to idTeam)
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

    fun addToFavorite(context: Context, data: Team) {

        doAsync {

            val result = try {

                context.database.use {
                    insert(Team.TABLE_TEAMS,
                            Team.ID_TEAM to data.idTeam,
                            Team.TEAM_BADGE to data.strTeamBadge,
                            Team.TEAM to data.strTeam,
                            Team.FORMED_YEAR to data.intFormedYear,
                            Team.STADIUM to data.strStadium,
                            Team.DESCRIPTION to data.strDescriptionEN)
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