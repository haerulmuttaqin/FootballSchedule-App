package com.haerul.footballapp.ui.match.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.haerul.footballapp.R
import com.haerul.footballapp.R.drawable.ic_add_to_favorites
import com.haerul.footballapp.R.drawable.ic_added_to_favorites
import com.haerul.footballapp.api.ApiRepository
import com.haerul.footballapp.model.Match
import com.haerul.footballapp.model.Team
import com.haerul.footballapp.ui.favorite.match.FavoriteMatchFragment.Companion.POSITION_DELETE
import com.haerul.footballapp.util.KeyValue.Companion.KEY_MATCH_DETAIL
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_match_detail.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.toast
import android.content.Intent
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import com.haerul.footballapp.util.*
import com.haerul.footballapp.util.KeyValue.Companion.KEY_MATCH_TYPE
import java.util.*


class MatchDetailActivity : AppCompatActivity(), MatchDetailView {

    private lateinit var id: String
    private var matchType: String? = null
    private lateinit var itemMatch: Match
    private lateinit var presenter: MatchDetailPresenter
    private var isPosition: Int? = null
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.match_detail)

        matchType = intent.getStringExtra(KEY_MATCH_TYPE)
        itemMatch = intent.getParcelableExtra(KEY_MATCH_DETAIL)
        setupData(itemMatch)

        presenter = MatchDetailPresenter(this, ApiRepository(), Gson())
        presenter.getMatches(itemMatch.idHomeTeam, itemMatch.idAwayTeam)
        isFavorite = presenter.getFavoriteState(this, itemMatch.idEvent.toString())

    }

    @SuppressLint("SetTextI18n")
    private fun setupData(item: Match) {

        val dateEvent: String? = item.dateEvent?.let { getStringDate(it) } ?: "-"
        val timeEvent: String? = item.strTime?.let { getStringTime(it) } ?: "-:-"
        date_match.text = "$dateEvent | $timeEvent"

        val homeScore: String? = item.intHomeScore?.let { it } ?: "?"
        val awayScore: String? = item.intAwayScore?.let { it } ?: "?"
        score.text = "$homeScore " + getString(R.string.strip) + " $awayScore"

        home_team.text = item.strHomeTeam
        home_goals.text = item.strHomeGoalDetails
        home_shots.text = item.intHomeShots
        home_goalskeeper.text = item.strHomeLineupGoalkeeper
        home_defense.text = item.strHomeLineupDefense
        home_midfield.text = item.strHomeLineupMidfield
        home_forward.text = item.strHomeLineupForward
        home_substitutes.text = item.strHomeLineupSubstitutes
        away_team.text = item.strAwayTeam
        away_goals.text = item.strAwayGoalDetails
        away_shots.text = item.intAwayShots
        away_goalskeeper.text = item.strAwayLineupGoalkeeper
        away_defense.text = item.strAwayLineupDefense
        away_midfield.text = item.strAwayLineupMidfield
        away_forward.text = item.strAwayLineupForward
        away_substitutes.text = item.strAwayLineupSubstitutes

        id = item.idEvent.toString()
        isPosition = intent.getIntExtra(POSITION_DELETE, 0)
    }

    override fun showLoading() {
        progress_circular.visible()
    }

    override fun hideLoading() {
        progress_circular.invisible()
    }

    override fun showTeam(homeTeam: List<Team>, awayTeam: List<Team>) {
        Picasso.get()
                .load(homeTeam[0].strTeamBadge)
                .placeholder(R.drawable.ic_badge)
                .error(R.drawable.ic_badge)
                .into(badge_home)

        Picasso.get()
                .load(awayTeam[0].strTeamBadge)
                .placeholder(R.drawable.ic_badge)
                .error(R.drawable.ic_badge)
                .into(badge_away)
    }

    override fun errorLoading(t: String?) {
        toast("Failed to load Team logo! \n $t")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        if (matchType.isNullOrEmpty()) menuItem?.findItem(R.id.add_to_calendar)?.isVisible = false
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_calendar -> {
                val calendar = Calendar.getInstance(Locale.getDefault())
                calendar.set(2017, Calendar.OCTOBER, 1, 9, 0)
                addEvent(
                        "${itemMatch.strHomeTeam} vs ${itemMatch.strAwayTeam}",
                        getCalendar(itemMatch.dateEvent.toString(), itemMatch.strTime.toString())
                )

                true
            }
            R.id.add_to_favorite -> {
                if (isFavorite) {
                    presenter.removeFromFavorite(this, id)
                }
                else presenter.addToFavorite(this, itemMatch)

                isFavorite = !isFavorite
                setFavorite()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onAddToFavorite(result: String?) {
        setResult(Activity.RESULT_CANCELED) //when condition if users added again to favorite after removing
        nested_scroll.snackbar(result.toString())
    }

    override fun onRemoveFromFavorite(result: String?) {
        nested_scroll.snackbar(result.toString())
        val data = intent
        data.putExtra("data_match", isPosition)
        setResult(Activity.RESULT_OK, data)
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(1)?.icon = ContextCompat.getDrawable(this, ic_added_to_favorites)
        else
            menuItem?.getItem(1)?.icon = ContextCompat.getDrawable(this, ic_add_to_favorites)
    }


    private fun addEvent(title: String, begin: Long) {
        val intent = Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .setType("vnd.android.cursor.item/event")
                .putExtra(Events.TITLE, title)
                .putExtra(Events.HAS_ALARM, true)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

}