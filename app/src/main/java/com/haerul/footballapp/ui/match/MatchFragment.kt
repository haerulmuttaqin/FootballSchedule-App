package com.haerul.footballapp.ui.match

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.haerul.footballapp.R
import com.haerul.footballapp.ui.adapter.ViewPagerAdapter
import com.haerul.footballapp.ui.match.last.LastMatchFragment
import com.haerul.footballapp.ui.match.next.NextMatchFragment
import com.haerul.footballapp.ui.match.search.MatchSearchFragment
import com.haerul.footballapp.util.FragmentTransaction
import com.haerul.footballapp.util.KeyValue.Companion.TAG_MATCH_SEARCH
import com.haerul.footballapp.util.makeAnimation
import kotlinx.android.synthetic.main.fragment_match.*
import kotlinx.android.synthetic.main.fragment_team.*

class MatchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        with(activity as AppCompatActivity){
            setSupportActionBar(toolbar_match)
            setTitle(R.string.title_matches)
            view_pager_match.adapter = ViewPagerAdapter(childFragmentManager, mapOf(
                    getString(R.string.next_match) to NextMatchFragment(),
                    getString(R.string.last_match) to LastMatchFragment()
            ))
            tab_layout_match.setupWithViewPager(view_pager_match)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu, menu)
        menu?.findItem(R.id.action_search)?.title = getString(R.string.match_search)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_search -> {
                FragmentTransaction.pushFragments(activity, TAG_MATCH_SEARCH, MatchSearchFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            makeAnimation(context, recycler_view, R.anim.zoom_in_to_view)
        }
    }
}
