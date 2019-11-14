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
import com.ydhnwb.moviecatalogue.adapters.MovieAdapter
import com.ydhnwb.moviecatalogue.viewmodels.MovieState
import com.ydhnwb.moviecatalogue.viewmodels.MovieViewModel
import kotlinx.android.synthetic.main.fragment_nowplaying.view.*

class NowPlayingFragment : Fragment(){
    private lateinit var movieViewModel : MovieViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        movieViewModel.getNowPlayingMovies()
        return inflater.inflate(R.layout.fragment_nowplaying, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            view.rv_nowplaying.apply {
                layoutManager = GridLayoutManager(activity, 2)
                adapter = MovieAdapter(mutableListOf(), activity!!)
            }
        }else{
            view.rv_nowplaying.apply {
                layoutManager = GridLayoutManager(activity, 4)
                adapter = MovieAdapter(mutableListOf(), activity!!)
            }
        }


        movieViewModel.nowPlayingMovies().observe(this, Observer {
            view.rv_nowplaying.adapter?.let {a ->
                if(a is MovieAdapter){
                    a.setMovies(it)
                }
            }
        })
        movieViewModel.state().observer(this, Observer {
            when(it){
                is MovieState.IsLoading -> {
                    if(it.state){
                        view.loading.visibility = View.VISIBLE
                    }else{
                        view.loading.visibility = View.GONE
                    }
                }
                is MovieState.ShowToast -> Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}