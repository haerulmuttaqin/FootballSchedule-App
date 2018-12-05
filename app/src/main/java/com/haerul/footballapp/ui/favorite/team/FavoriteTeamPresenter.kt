package com.haerul.footballapp.ui.favorite.team

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.haerul.footballapp.helper.database
import com.haerul.footballapp.model.Team
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class FavoriteTeamPresenter(private val view: FavoriteTeamView, private val context: Context?) {

    fun getFavorites() {
        view.showLoading()

        doAsync {
            var favorites: List<Team>? = null
            var exception: SQLiteConstraintException? = null

            try {
                context?.database?.use {
                    val result = select(Team.TABLE_TEAMS)
                    favorites = result.parseList(classParser<Team>())
                }
            } catch (e: SQLiteConstraintException) {
                exception = e
            }

            uiThread {
                view.hideLoading()
                when {
                    exception != null -> view.errorLoading(exception.message)
                    favorites.isNullOrEmpty() -> view.errorLoading("No team favorite yet, or " + exception?.message)
                    else -> favorites?.let { it -> view.showFavorite(it) }
                }
            }
        }
    }
}