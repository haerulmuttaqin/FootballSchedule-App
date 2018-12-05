package com.haerul.footballapp.ui.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haerul.footballapp.R
import com.haerul.footballapp.model.Match
import com.haerul.footballapp.util.getStringDate
import com.haerul.footballapp.util.getStringTime
import kotlinx.android.synthetic.main.item_match.view.*

class MatchAdapter(private val matches: List<Match>,
                   private val listener: (Match, Int) -> Unit) : RecyclerView.Adapter<MatchAdapter.TeamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return TeamViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_match, parent, false))
    }

    override fun getItemCount() = matches.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bindItem(matches[position], listener, position)
    }

    inner class TeamViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        @SuppressLint("SetTextI18n")
        fun bindItem(match: Match, listener: (Match, Int) -> Unit, position: Int) {

            val dateEvent: String? = match.dateEvent?.let { getStringDate(it) } ?: "-"
            val timeEvent: String? = match.strTime?.let { getStringTime(it) } ?: "-:-"
            view.date_match.text = "$dateEvent | $timeEvent"
            view.home_team.text = match.strHomeTeam
            view.home_score.text = match.intHomeScore?.let { it } ?: "?"
            view.away_score.text = match.intAwayScore?.let { it } ?: "?"
            view.away_team.text = match.strAwayTeam
            view.setOnClickListener { listener(match, position) }
        }
    }
}
