package com.haerul.footballapp.ui.match.next

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.haerul.footballapp.R
import com.haerul.footballapp.api.ApiRepository
import com.haerul.footballapp.model.League
import com.haerul.footballapp.model.LeagueResponse
import com.haerul.footballapp.model.Match
import com.haerul.footballapp.ui.adapter.MatchAdapter
import com.haerul.footballapp.ui.match.detail.MatchDetailActivity
import com.haerul.footballapp.util.KeyValue.Companion.KEY_MATCH_DETAIL
import com.haerul.footballapp.util.KeyValue.Companion.KEY_MATCH_TYPE
import com.haerul.footballapp.util.invisible
import com.haerul.footballapp.util.visible
import kotlinx.android.synthetic.main.fragment_match_child.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity

class NextMatchFragment : Fragment(), NextMatchView {

    private lateinit var presenter: NextMatchPresenter
    private lateinit var adapter: MatchAdapter
    private lateinit var league: League
    private var itemMatches: MutableList<Match> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_match_child, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MatchAdapter(itemMatches) { match: Match, _: Int ->
            startActivity<MatchDetailActivity>(KEY_MATCH_DETAIL to match, KEY_MATCH_TYPE to "next")
        }

        recycler_view.adapter = adapter

        presenter = NextMatchPresenter(this, ApiRepository(), Gson())
        presenter.getLeagues()
        swipe_refresh.onRefresh {
            presenter.getMatches(league.idLeague.toString())
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

    override fun showLeagues(data: LeagueResponse) {
        spinner.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, data.leagues)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                league = spinner.selectedItem as League
                presenter.getMatches(league.idLeague.toString())
            }
        }
    }

    override fun showMatches(data: List<Match>) {
        try {
            hideMessageOnLoadingErr()
        }catch (e: Exception) {
            e.printStackTrace()
        }
        itemMatches.clear()
        itemMatches.addAll(data)
        adapter.notifyDataSetChanged()
        recycler_view.smoothScrollToPosition(0)
        spinner_container.visible()
    }

    override fun errorLoading(e: String?) {
        itemMatches.clear()
        adapter.notifyDataSetChanged()
        showMessageOnLoadingErr(
                getString(R.string.no_result) + " " + getString(R.string.next_match) +
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
}
