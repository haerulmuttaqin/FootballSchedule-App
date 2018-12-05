package com.haerul.footballapp.api

import android.net.Uri
import com.haerul.footballapp.BuildConfig

object TheSportDBApi {

    fun getLeague(): String {
        return "${BuildConfig.BASE_URL}/api/v1/json/${BuildConfig.TSDB_API_KEY}/all_leagues.php"
    }

    fun getLastMatch(id: String?): String {
        return "${BuildConfig.BASE_URL}/api/v1/json/${BuildConfig.TSDB_API_KEY}/eventspastleague.php?id=$id"
    }

    fun getNextMatch(id: String?): String {
        return "${BuildConfig.BASE_URL}/api/v1/json/${BuildConfig.TSDB_API_KEY}/eventsnextleague.php?id=$id"
    }

    fun getMatchSearch(keyword: String?): String {
        return "${BuildConfig.BASE_URL}/api/v1/json/${BuildConfig.TSDB_API_KEY}/searchevents.php?e=$keyword"
    }

    fun getTeamDetail(id: String?): String {
        return "${BuildConfig.BASE_URL}/api/v1/json/${BuildConfig.TSDB_API_KEY}/lookupteam.php?id=$id"
    }

    fun getTeams(league: String?): String {
        return try {
            Uri.parse(BuildConfig.BASE_URL).buildUpon()
                    .appendPath("api")
                    .appendPath("v1")
                    .appendPath("json")
                    .appendPath(BuildConfig.TSDB_API_KEY)
                    .appendPath("search_all_teams.php")
                    .appendQueryParameter("l", league)
                    .build()
                    .toString()
        } catch (e: Exception) {
            "${BuildConfig.BASE_URL}/api/v1/json/${BuildConfig.TSDB_API_KEY}/search_all_teams.php?l=$league"
        }
    }

    fun getTeamSearch(keyword: String?): String {
        return "${BuildConfig.BASE_URL}/api/v1/json/${BuildConfig.TSDB_API_KEY}/searchteams.php?t=$keyword"

    }

    fun getPlayers(teamId: String?): String {
        return "${BuildConfig.BASE_URL}/api/v1/json/${BuildConfig.TSDB_API_KEY}/lookup_all_players.php?id=$teamId"
    }

}