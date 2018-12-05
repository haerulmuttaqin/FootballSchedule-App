package com.haerul.footballapp.ui.teams.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.haerul.footballapp.R
import com.haerul.footballapp.model.Player
import com.haerul.footballapp.util.KeyValue
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_player_detail.*

class PlayerDetailActivity : AppCompatActivity() {

    private lateinit var itemPlayer: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        itemPlayer = intent.getParcelableExtra(KeyValue.KEY_PLAYER_DETAIL)
        setupData(itemPlayer)
    }

    private fun setupData(item: Player) {

        Picasso.get().load(item.strFanart1).into(player_banner)
        Picasso.get().load(item.strCutout)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(player_badge)

        collapsing_toolbar.title = item.strPlayer

        player_position.text = item.strPosition
        player_weight.text = item.strWeight
        player_height.text = item.strHeight
        player_description.text = item.strDescriptionEN
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}