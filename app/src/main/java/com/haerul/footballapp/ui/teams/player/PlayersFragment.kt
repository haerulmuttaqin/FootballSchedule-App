package com.haerul.footballapp.ui.teams.player

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.gson.Gson
import com.haerul.footballapp.R
import com.haerul.footballapp.api.ApiRepository
import com.haerul.footballapp.model.Player
import com.haerul.footballapp.ui.adapter.PlayerAdapter
import com.haerul.footballapp.ui.teams.detail.PlayerDetailActivity
import com.haerul.footballapp.util.GridItemDecoration
import com.haerul.footballapp.util.KeyValue.Companion.KEY_EXTRA_PARAM
import com.haerul.footballapp.util.KeyValue.Companion.KEY_PLAYER_DETAIL
import com.haerul.footballapp.util.invisible
import com.haerul.footballapp.util.visible
import kotlinx.android.synthetic.main.fragment_favorite_match.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity

class PlayersFragment : Fragment(), PlayersView {

    fun newInstance(args: String): PlayersFragment {
        val fragment = PlayersFragment()
        val bundle = Bundle()

        bundle.putString(KEY_EXTRA_PARAM, args)
        fragment.arguments = bundle

        return fragment
    }

    private lateinit var presenter: PlayersPresenter
    private lateinit var adapter: PlayerAdapter
    private var itemPlayer: MutableList<Player> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player_child, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlayerAdapter(itemPlayer) { player: Player, _: Int, v: View ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                val img = v.findViewById<ImageView>(R.id.player_badge)
                val options = ActivityOptions.makeSceneTransitionAnimation(activity, img, ViewCompat.getTransitionName(img))
                val intent = Intent(activity, PlayerDetailActivity::class.java)
                intent.putExtra(KEY_PLAYER_DETAIL, player)
                startActivity(intent, options.toBundle())
            } else {
                startActivity<PlayerDetailActivity>(KEY_PLAYER_DETAIL to player)
            }
        }

        recycler_view.layoutManager = GridLayoutManager(context, 2)
        recycler_view.addItemDecoration(GridItemDecoration(16, 2))
        recycler_view.adapter = adapter

        presenter = PlayersPresenter(this, ApiRepository(), Gson())
        presenter.getPlayers(arguments?.getString(KEY_EXTRA_PARAM))
        swipe_refresh.onRefresh {
            presenter.getPlayers(arguments?.getString(KEY_EXTRA_PARAM))
        }
    }

    override fun showLoading() {
        swipe_refresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipe_refresh?.isRefreshing = false
    }

    override fun showPlayers(data: List<Player>) {
        try {
            hideMessageOnLoadingErr()
        }catch (e: Exception) {
            e.printStackTrace()
        }
        itemPlayer.clear()
        itemPlayer.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun errorLoading(e: String?) {
        itemPlayer.clear()
        adapter.notifyDataSetChanged()
        showMessageOnLoadingErr(
                getString(R.string.no_result) + " " + getString(R.string.players) +
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