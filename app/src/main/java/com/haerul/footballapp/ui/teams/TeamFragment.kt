package com.haerul.footballapp.ui.teams

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.haerul.footballapp.R
import com.haerul.footballapp.api.ApiRepository
import com.haerul.footballapp.model.League
import com.haerul.footballapp.model.LeagueResponse
import com.haerul.footballapp.model.Team
import com.haerul.footballapp.ui.adapter.TeamAdapter
import com.haerul.footballapp.ui.teams.detail.TeamDetailActivity
import com.haerul.footballapp.ui.teams.search.TeamSearchFragment
import com.haerul.footballapp.util.*
import com.haerul.footballapp.util.KeyValue.Companion.KEY_TEAM_DETAIL
import com.haerul.footballapp.util.KeyValue.Companion.TAG_TEAM
import kotlinx.android.synthetic.main.fragment_team.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity

class TeamFragment : Fragment(), TeamView {

    private lateinit var presenter: TeamPresenter
    private lateinit var adapter: TeamAdapter
    private lateinit var league: League
    private var itemTeams: MutableList<Team> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        with(activity as AppCompatActivity) {
            setSupportActionBar(toolbar)
            setTitle(R.string.title_teams)
        }
        adapter = TeamAdapter(itemTeams, TAG_TEAM) { team: Team, _: Int ->
            startActivity<TeamDetailActivity>(KEY_TEAM_DETAIL to team)
        }

        recycler_view.layoutManager = GridLayoutManager(context, 2)
        recycler_view.addItemDecoration(GridItemDecoration(16, 2))
        recycler_view.adapter = adapter
        presenter = TeamPresenter(this, ApiRepository(), Gson())
        presenter.getLeagues()
        swipe_refresh.onRefresh {
            presenter.getTeams(league.strLeague.toString())
        }
    }

    override fun showLoading() {
        swipe_refresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipe_refresh?.isRefreshing = false
    }

    override fun showLeagues(data: LeagueResponse) {
        spinner.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, data.leagues)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                league = spinner.selectedItem as League
                presenter.getTeams(league.strLeague.toString())
            }
        }
    }

    override fun showTeams(data: List<Team>) {
        try {
            hideMessageOnLoadingErr()
        }catch (e: Exception) {
            e.printStackTrace()
        }
        itemTeams.clear()
        itemTeams.addAll(data)
        adapter.notifyDataSetChanged()
        recycler_view.smoothScrollToPosition(0)
        spinner_container.visible()
    }

    override fun errorLoading(e: String?) {
        itemTeams.clear()
        adapter.notifyDataSetChanged()
        showMessageOnLoadingErr(
                getString(R.string.no_result) + " " + getString(R.string.teams) +
                        "\n $e")
    }

    private fun showMessageOnLoadingErr(msg: String){
        error_message.text = msg
        error_message.visible()
    }

    private fun hideMessageOnLoadingErr(){
        if (error_message != null) {
            error_message.text = ""
            error_message.invisible()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu, menu)
        menu?.findItem(R.id.action_search)?.title = getString(R.string.teams_search)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_search -> {
                FragmentTransaction.pushFragments(activity, KeyValue.TAG_TEAM_SEARCH, TeamSearchFragment())
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            makeAnimation(context, recycler_view, R.anim.zoom_in_to_view)
        }
    }
}
