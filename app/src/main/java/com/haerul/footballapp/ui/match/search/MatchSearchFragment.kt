package com.haerul.footballapp.ui.match.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.*
import com.google.gson.Gson
import com.haerul.footballapp.R
import com.haerul.footballapp.api.ApiRepository
import com.haerul.footballapp.model.Match
import com.haerul.footballapp.ui.adapter.MatchAdapter
import com.haerul.footballapp.ui.match.MatchFragment
import com.haerul.footballapp.ui.match.detail.MatchDetailActivity
import com.haerul.footballapp.util.FragmentTransaction
import com.haerul.footballapp.util.KeyValue
import com.haerul.footballapp.util.KeyValue.Companion.TAG_MATCH
import com.haerul.footballapp.util.invisible
import com.haerul.footballapp.util.visible
import kotlinx.android.synthetic.main.fragment_search.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import android.view.LayoutInflater

class MatchSearchFragment : Fragment(), MatchSearchView {

    private lateinit var presenter: MatchSearchPresenter
    private lateinit var adapter: MatchAdapter
    private lateinit var keyword: String
    private var itemMatches: MutableList<Match> = mutableListOf()

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
        adapter = MatchAdapter(itemMatches) { match: Match, _: Int ->
            startActivity<MatchDetailActivity>(KeyValue.KEY_MATCH_DETAIL to match)
        }

        recycler_view.adapter = adapter

        presenter = MatchSearchPresenter(this, ApiRepository(), Gson())

        swipe_refresh.onRefresh {
            presenter.getSearchMatches(keyword)
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

    override fun showSearchResult(data: List<Match>) {
        try {
            hideMessageOnLoadingErr()
        }catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            image_search.invisible()
            itemMatches.clear()
            itemMatches.addAll(data)
            adapter.notifyDataSetChanged()
            recycler_view.smoothScrollToPosition(0)
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun errorLoading(e: String?) {
        itemMatches.clear()
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

        searchView.queryHint = getString(R.string.search_match_hint)
        searchView.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                keyword = query.toString()
                presenter.getSearchMatches(keyword)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                keyword = query.toString()
                if (keyword.isEmpty()) {
                    itemMatches.clear()
                    adapter.notifyDataSetChanged()
                    image_search.visible()
                } else presenter.getSearchMatches(keyword)
                return true
            }
        })

        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean { return true }
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                FragmentTransaction.pushFragments(activity, TAG_MATCH, MatchFragment())
                return true
            }
        })

        menu.findItem(R.id.action_search_view)?.expandActionView()
        return super.onCreateOptionsMenu(menu, inflater)
    }

}