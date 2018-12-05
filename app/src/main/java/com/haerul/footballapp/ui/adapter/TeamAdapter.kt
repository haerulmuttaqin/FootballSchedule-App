package com.haerul.footballapp.ui.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haerul.footballapp.R
import com.haerul.footballapp.model.Team
import com.haerul.footballapp.util.KeyValue
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_team.view.*

class TeamAdapter(private val teams: List<Team>,
                  private val type: String,
                  private val listener: (Team, Int) -> Unit) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return if(type == KeyValue.TAG_TEAM_SEARCH) {
            TeamViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_team_search, parent, false))
        }
        else {
            TeamViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false))
        }
    }

    override fun getItemCount() = teams.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bindItem(teams[position], listener, position)
    }

    inner class TeamViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        @SuppressLint("SetTextI18n")
        fun bindItem(team: Team, listener: (Team, Int) -> Unit, position: Int) {

            Picasso.get()
                    .load(team.strTeamBadge)
                    .placeholder(R.drawable.ic_badge)
                    .error(R.drawable.ic_badge)
                    .into(view.team_badge)

            view.team_name.text = team.strTeam

            view.setOnClickListener { listener(team, position) }
        }
    }
}

