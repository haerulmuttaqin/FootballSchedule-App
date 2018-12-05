package com.haerul.footballapp.ui.favorite.match

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haerul.footballapp.R
import com.haerul.footballapp.model.Match
import com.haerul.footballapp.ui.adapter.MatchAdapter
import com.haerul.footballapp.ui.match.detail.MatchDetailActivity
import com.haerul.footballapp.util.KeyValue.Companion.KEY_MATCH_DETAIL
import com.haerul.footballapp.util.invisible
import com.haerul.footballapp.util.visible
import kotlinx.android.synthetic.main.fragment_match_child.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivityForResult

class FavoriteMatchFragment : Fragment(), FavoriteMatchView {

    private lateinit var adapter: MatchAdapter
    private var itemFavorite: MutableList<Match> = mutableListOf()
    companion object {
        private const val CODE_DELETE: Int = 100
        const val POSITION_DELETE: String = "position"
    }
    private lateinit var presenter: FavoriteMatchPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MatchAdapter(itemFavorite) { match: Match, i: Int ->
            startActivityForResult<MatchDetailActivity>(CODE_DELETE,
                    POSITION_DELETE to i, KEY_MATCH_DETAIL to match)
        }

        recycler_view.adapter = adapter
        setData()
        swipe_refresh.onRefresh {
            itemFavorite.clear()
            setData()
        }
    }

    private fun setData(){
        presenter = FavoriteMatchPresenter(this, context)
        presenter.getFavorites()
    }

    override fun showFavorite(favorites: List<Match>) {
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
        if (resultCode == RESULT_OK && data != null) {
            when(requestCode){
                CODE_DELETE -> {
                    val position: Int = data.getIntExtra("data_match", 0)
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