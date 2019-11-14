package com.ydhnwb.moviecatalogue

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.ydhnwb.moviecatalogue.adapters.MovieAdapter
import com.ydhnwb.moviecatalogue.viewmodels.MovieState
import com.ydhnwb.moviecatalogue.viewmodels.MovieViewModel
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*

class SearchActivity : AppCompatActivity() {
    companion object { var hasOpen = false }
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { finish() }
        rv_search.apply {
            layoutManager = GridLayoutManager(this@SearchActivity, 2)
            adapter = MovieAdapter(mutableListOf(), this@SearchActivity)
        }
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        movieViewModel.searchResults().observe(this, Observer {
            rv_search.adapter?.let {a ->
                if(a is MovieAdapter){
                    a.setMovies(it)
                }
            }
        })
        movieViewModel.state().observe(this, Observer { handleState(it) })
        if(!hasOpen){
            hasOpen = true
            handleSearch(getQuery())
        }
    }

    private fun handleState(it : MovieState){
        when(it){
            is MovieState.ShowToast -> Toast.makeText(this@SearchActivity, it.message, Toast.LENGTH_LONG).show()
            is MovieState.IsLoading -> {
                if(it.state){
                    loading.visibility = View.VISIBLE
                }else{
                    loading.visibility = View.GONE
                }
            }
        }
    }

    private fun getQuery() : String = intent.getStringExtra("QUERY_SEARCH")!!

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu!!.findItem(R.id.action_search).actionView as SearchView
        searchView.queryHint = getString(R.string.hint_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if(it.isNotEmpty()){ handleSearch(it) }
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean = false
        })
        return true
    }

    private fun handleSearch(query : String) {
        supportActionBar?.title = getString(R.string.title_search)+ " $query"
        movieViewModel.searchMovies(query)
    }

}
