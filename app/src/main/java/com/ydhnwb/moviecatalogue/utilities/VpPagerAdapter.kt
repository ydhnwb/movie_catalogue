package com.ydhnwb.moviecatalogue.utilities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ydhnwb.moviecatalogue.fragments.NowPlayingFragment
import com.ydhnwb.moviecatalogue.fragments.UpcomingFragment

class VpPagerAdapter(fm: FragmentManager, poster : String, title : String, rating : Float, desc : String, vote_count : Long): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val pages = listOf(VpMainFragment.instance(poster, rating, title, vote_count), VpInfoFragment.instance(desc))

    override fun getItem(position: Int): Fragment = pages[position]

    override fun getCount() = pages.size
}


class FragmentTabAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val pages = listOf(NowPlayingFragment(), UpcomingFragment())
    private val titles = mutableListOf<String>()

    fun addTitles(title : String){ titles.add(title)}

    override fun getItem(position: Int): Fragment = pages[position]

    override fun getCount() = pages.size

    override fun getPageTitle(position: Int): CharSequence? = titles[position]
}