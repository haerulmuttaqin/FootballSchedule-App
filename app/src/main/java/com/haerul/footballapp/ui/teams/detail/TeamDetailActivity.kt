package com.haerul.footballapp.ui.teams.detail

import android.app.Activity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.haerul.footballapp.R
import com.haerul.footballapp.R.drawable.ic_add_to_favorites
import com.haerul.footballapp.R.drawable.ic_added_to_favorites
import com.haerul.footballapp.model.Team
import com.haerul.footballapp.ui.adapter.ViewPagerAdapter
import com.haerul.footballapp.ui.favorite.match.FavoriteMatchFragment.Companion.POSITION_DELETE
import com.haerul.footballapp.ui.teams.overview.OverviewFragment
import com.haerul.footballapp.ui.teams.player.PlayersFragment
import com.haerul.footballapp.util.KeyValue.Companion.KEY_TEAM_DETAIL
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_team_detail.*
import org.jetbrains.anko.design.snackbar

class TeamDetailActivity : AppCompatActivity(), TeamDetailView {

    private lateinit var id: String
    private lateinit var itemTeam: Team
    private lateinit var presenter: TeamDetailPresenter
    private var isPosition: Int? = null
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        itemTeam = intent.getParcelableExtra(KEY_TEAM_DETAIL)
        setupData(itemTeam)

        presenter = TeamDetailPresenter(this)
        isFavorite = presenter.getFavoriteState(this, itemTeam.idTeam.toString())
    }

    private fun setupData(item: Team) {

        Picasso.get()
                .load(item.strTeamBadge)
                .placeholder(R.drawable.ic_badge)
                .error(R.drawable.ic_badge)
                .into(team_badge)

        team_name.text = item.strTeam
        team_years.text = item.intFormedYear
        team_stadium.text = item.strStadium

        id = item.idTeam.toString()
        isPosition = intent.getIntExtra(POSITION_DELETE, 0)

        setupViewPager(item)
    }

    private fun setupViewPager(item: Team) {
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager, mapOf(
                getString(R.string.overview) to OverviewFragment().newInstance(item.strDescriptionEN.toString()),
                getString(R.string.players) to PlayersFragment().newInstance(item.idTeam.toString())
        ))
        tab_layout.setupWithViewPager(view_pager)
    }

    override fun onAddToFavorite(result: String?) {
        setResult(Activity.RESULT_CANCELED) //when condition if users added again to favorite after removing
        view_pager.snackbar(result.toString())
    }

    override fun onRemoveFromFavorite(result: String?) {
        view_pager.snackbar(result.toString())
        val data = intent
        data.putExtra("data_team", isPosition)
        setResult(Activity.RESULT_OK, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        menuItem?.findItem(R.id.add_to_calendar)?.isVisible = false
        setFavorite()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if (isFavorite) {
                    presenter.removeFromFavorite(this, id)
                }
                else presenter.addToFavorite(this, itemTeam)

                isFavorite = !isFavorite
                setFavorite()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(1)?.icon = ContextCompat.getDrawable(this, ic_added_to_favorites)
        else
            menuItem?.getItem(1)?.icon = ContextCompat.getDrawable(this, ic_add_to_favorites)
    }
}