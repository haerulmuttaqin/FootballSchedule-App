package com.haerul.footballapp.ui.favorite.match

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import com.haerul.footballapp.helper.database
import com.haerul.footballapp.model.Match
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class FavoriteMatchPresenter(private val view: FavoriteMatchView, private val context: Context?) {

    fun getFavorites() {
        view.showLoading()

        doAsync {
            var favorites: List<Match>? = null
            var exception: SQLiteConstraintException? = null

            try {
                context?.database?.use {
                    val result = select(Match.TABLE_MATCHES)
                    favorites = result.parseList(classParser<Match>())
                }
            } catch (e: SQLiteConstraintException) {
                exception = e
            }

            uiThread {
                view.hideLoading()
                when {
                    exception != null -> view.errorLoading(exception.message)
                    favorites.isNullOrEmpty() -> view.errorLoading("No match favorite yet, or " + exception?.message)
                    else -> favorites?.let { it -> view.showFavorite(it) }
                }
            }
        }
    }
}