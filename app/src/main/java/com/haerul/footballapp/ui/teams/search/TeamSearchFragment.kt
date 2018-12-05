package com.haerul.footballapp.ui.teams.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.google.gson.Gson
import com.haerul.footballapp.R
import com.haerul.footballapp.api.ApiRepository
import com.haerul.footballapp.model.Team
import com.haerul.footballapp.ui.adapter.TeamAdapter
import com.haerul.footballapp.ui.teams.TeamFragment
import com.haerul.footballapp.ui.teams.detail.TeamDetailActivity
import com.haerul.footballapp.util.FragmentTransaction
import com.haerul.footballapp.util.KeyValue
import com.haerul.footballapp.util.KeyValue.Companion.TAG_TEAM
import com.haerul.footballapp.util.KeyValue.Companion.TAG_TEAM_SEARCH
import com.haerul.footballapp.util.invisible
import com.haerul.footballapp.util.visible
import kotlinx.android.synthetic.main.fragment_search.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity

class TeamSearchFragment : Fragment(), TeamSearchView {

    private lateinit var presenter: TeamSearchPresenter
    private lateinit var adapter: TeamAdapter
    private lateinit var keyword: String
    private var itemTeams: MutableList<Team> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        with(activity as AppCompatActivity){
            setSupportActionBar(toolbar_search)
        }
        adapter = TeamAdapter(itemTeams, TAG_TEAM_SEARCH) { team: Team, _: Int ->
            startActivity<TeamDetailActivity>(KeyValue.KEY_TEAM_DETAIL to team)
        }

        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = adapter
        presenter = TeamSearchPresenter(this, ApiRepository(), Gson())

        swipe_refresh.onRefresh {
            presenter.getSearchTeam(keyword)
        }
    }

    override fun showLoading() {
        try {
            swipe_refresh.isRefreshing = true
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun hideLoading() {
        swipe_refresh?.isRefreshing = false
    }

    override fun showSearchResult(data: List<Team>) {
        try {
            hideMessageOnLoadingErr()
        }catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            image_search.invisible()
            itemTeams.clear()
            itemTeams.addAll(data)
            adapter.notifyDataSetChanged()
            recycler_view.smoothScrollToPosition(0)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun errorLoading(e: String?) {
        itemTeams.clear()
        adapter.notifyDataSetChanged()
        showMessageOnLoadingErr(
                getString(R.string.no_result) + " '$keyword'" +
                        "\n $e")
        image_search.visible()
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
        inflater?.inflate(R.menu.menu_search, menu)

        val searchItem = menu?.findItem(R.id.action_search_view)
        val searchView = menu?.findItem(R.id.action_search_view)?.actionView as SearchView

        searchView.queryHint = getString(R.string.search_team_hint)
        searchView.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                keyword = query.toString()
                presenter.getSearchTeam(keyword)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                keyword = query.toString()
                if (keyword.isEmpty()) {
                    itemTeams.clear()
                    adapter.notifyDataSetChanged()
                    image_search.visible()
                } else presenter.getSearchTeam(keyword)
                return true
            }
        })

        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean { return true }
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                FragmentTransaction.pushFragments(activity, TAG_TEAM, TeamFragment())
                return true
            }
        })

        menu.findItem(R.id.action_search_view)?.expandActionView()
        return super.onCreateOptionsMenu(menu, inflater)
    }

}