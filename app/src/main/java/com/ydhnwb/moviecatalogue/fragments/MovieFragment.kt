package com.ydhnwb.moviecatalogue.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ydhnwb.moviecatalogue.R
import com.ydhnwb.moviecatalogue.utilities.FragmentTabAdapter
import kotlinx.android.synthetic.main.fragment_movie.view.*

class MovieFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_movie, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fm =  FragmentTabAdapter(childFragmentManager)
        fm.addTitles(resources.getString(R.string.tab_now_playing))
        fm.addTitles(resources.getString(R.string.tab_upcoming))
        view.movie_viewpager.adapter = fm
        view.movie_tabs.setupWithViewPager(view.movie_viewpager)
    }
}