package com.haerul.footballapp.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.haerul.footballapp.model.Match
import com.haerul.footballapp.model.Team
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(ctx: Context)
    : ManagedSQLiteOpenHelper(ctx, "Favorites.db", null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        //creating db
        db.createTable(Match.TABLE_MATCHES, true,
                Match.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                Match.ID_EVENT to TEXT,
                Match.DATE to TEXT,
                Match.TIME to TEXT,
                Match.HOME_ID to TEXT,
                Match.HOME_TEAM to TEXT,
                Match.HOME_SCORE to TEXT,
                Match.HOME_FORMATION to TEXT,
                Match.HOME_GOAL_DETAILS to TEXT,
                Match.HOME_SHOTS to TEXT,
                Match.HOME_LINEUP_GOALKEEPER to TEXT,
                Match.HOME_LINEUP_DEFENSE to TEXT,
                Match.HOME_LINEUP_MIDFIELD to TEXT,
                Match.HOME_LINEUP_FORWARD to TEXT,
                Match.HOME_LINEUP_SUBSTITUTES to TEXT,
                Match.AWAY_ID to TEXT,
                Match.AWAY_TEAM to TEXT,
                Match.AWAY_SCORE to TEXT,
                Match.AWAY_FORMATION to TEXT,
                Match.AWAY_GOAL_DETAILS to TEXT,
                Match.AWAY_SHOTS to TEXT,
                Match.AWAY_LINEUP_GOALKEEPER to TEXT,
                Match.AWAY_LINEUP_DEFENSE to TEXT,
                Match.AWAY_LINEUP_MIDFIELD to TEXT,
                Match.AWAY_LINEUP_FORWARD to TEXT,
                Match.AWAY_LINEUP_SUBSTITUTES to TEXT)

        db.createTable(Team.TABLE_TEAMS, true,
                Team.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                Team.ID_TEAM to TEXT,
                Team.TEAM_BADGE to TEXT,
                Team.TEAM to TEXT,
                Team.FORMED_YEAR to TEXT,
                Team.STADIUM to TEXT,
                Team.DESCRIPTION to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //upgrade table
        db.dropTable(Match.TABLE_MATCHES, true)
        db.dropTable(Team.TABLE_TEAMS, true)
    }
}

//access ctx
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)