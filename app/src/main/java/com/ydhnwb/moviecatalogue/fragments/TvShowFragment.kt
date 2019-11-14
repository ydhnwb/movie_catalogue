package com.ydhnwb.moviecatalogue.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.ydhnwb.moviecatalogue.R
import com.ydhnwb.moviecatalogue.adapters.TvShowAdapter
import com.ydhnwb.moviecatalogue.viewmodels.TvShowState
import com.ydhnwb.moviecatalogue.viewmodels.TvShowViewModel
import kotlinx.android.synthetic.main.fragment_tvshow.view.*

class TvShowFragment : Fragment() {
    private lateinit var tvShowViewModel : TvShowViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel::class.java)
        tvShowViewModel.getTvShow()
        return inflater.inflate(R.layout.fragment_tvshow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            view.rv_tvshow.apply {
                layoutManager = GridLayoutManager(activity,2)
                adapter = TvShowAdapter(mutableListOf(), activity!!)
            }
        }else{
            view.rv_tvshow.apply {
                layoutManager = GridLayoutManager(activity, 3)
                adapter = TvShowAdapter(mutableListOf(), activity!!)
            }
        }
        tvShowViewModel.tvShows().observe(this, Observer {
            view.rv_tvshow.adapter?.let {a ->
                if(a is TvShowAdapter){
                    a.setTvShows(it)
                }
            }
        })

        tvShowViewModel.state().observer(this, Observer {
            when(it){
                is TvShowState.ShowToast -> Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                is TvShowState.IsLoading -> {
                    if(it.state){
                        view.loading.visibility = View.VISIBLE
                    }else{
                        view.loading.visibility = View.GONE
                    }
                }
            }
        })
    }
}