package com.haerul.footballapp.ui.favorite.team

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haerul.footballapp.R
import com.haerul.footballapp.model.Team
import com.haerul.footballapp.ui.adapter.TeamAdapter
import com.haerul.footballapp.ui.teams.detail.TeamDetailActivity
import com.haerul.footballapp.util.GridItemDecoration
import com.haerul.footballapp.util.KeyValue
import com.haerul.footballapp.util.invisible
import com.haerul.footballapp.util.visible
import kotlinx.android.synthetic.main.fragment_team.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivityForResult


class FavoriteTeamFragment : Fragment(), FavoriteTeamView {

    private lateinit var adapter: TeamAdapter
    private var itemFavorite: MutableList<Team> = mutableListOf()
    companion object {
        private const val CODE_DELETE: Int = 200
        const val POSITION_DELETE: String = "position"
    }
    private lateinit var presenter: FavoriteTeamPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TeamAdapter(itemFavorite, KeyValue.TAG_TEAM) { team: Team, i: Int ->
            startActivityForResult<TeamDetailActivity>(CODE_DELETE,
                    POSITION_DELETE to i, KeyValue.KEY_TEAM_DETAIL to team)
        }

        recycler_view.layoutManager = GridLayoutManager(context, 2)
        recycler_view.addItemDecoration(GridItemDecoration(16, 2))
        recycler_view.adapter = adapter

        setData()
        swipe_refresh.onRefresh {
            itemFavorite.clear()
            setData()
        }
    }

    private fun setData(){
        presenter = FavoriteTeamPresenter(this, context)
        presenter.getFavorites()
    }

    override fun showFavorite(favorites: List<Team>) {
        try {
            hideMessageOnLoadingErr()
        }catch (e: Exception) {
            e.printStackTrace()
        }
        swipe_refresh.isRefreshing = false
        itemFavorite.addAll(favorites)
        adapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        swipe_refresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipe_refresh.isRefreshing = false
    }

    override fun errorLoading(e: String?) {
        itemFavorite.clear()
        adapter.notifyDataSetChanged()
        try {
            showMessageOnLoadingErr(
                    getString(R.string.no_result) + " " + getString(R.string.favorites) +
                            "\n $e")
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when(requestCode){
                CODE_DELETE -> {
                    val position: Int = data.getIntExtra("data_team", 0)
                    itemFavorite.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    adapter.notifyItemRangeRemoved(position, itemFavorite.size)
                }
            }
        }
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