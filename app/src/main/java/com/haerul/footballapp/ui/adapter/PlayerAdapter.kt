package com.haerul.footballapp.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haerul.footballapp.R
import com.haerul.footballapp.model.Player
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_player.view.*

class PlayerAdapter(private val players: List<Player>,
                    private val listener: (Player, Int, View) -> Unit) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_player, parent, false))
    }

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bindItem(players[position], listener, position)
    }

    inner class PlayerViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        fun bindItem(player: Player, listener: (Player, Int, View) -> Unit, position: Int) {

            Picasso.get()
                    .load(player.strCutout)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(view.player_badge)

            view.player_name.text = player.strPlayer
            view.player_position.text = player.strPosition

            view.setOnClickListener { listener(player, position, view) }
        }
    }
}
