package com.haerul.footballapp.ui.teams.overview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haerul.footballapp.R
import com.haerul.footballapp.util.KeyValue.Companion.KEY_EXTRA_PARAM
import kotlinx.android.synthetic.main.fragment_team_overview.*

class OverviewFragment : Fragment() {

    fun newInstance(args: String): OverviewFragment {
        val fragment = OverviewFragment()
        val bundle = Bundle()

        bundle.putString(KEY_EXTRA_PARAM, args)
        fragment.arguments = bundle

        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_team_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        team_overview.text = arguments?.getString(KEY_EXTRA_PARAM)
    }
}
