package com.ydhnwb.moviecatalogue.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ydhnwb.moviecatalogue.R
import com.ydhnwb.moviecatalogue.adapters.MovieAdapter
import com.ydhnwb.moviecatalogue.databases.DatabaseHelper
import com.ydhnwb.moviecatalogue.models.Movie
import kotlinx.android.synthetic.main.fragment_favorite.view.*

class FavoriteFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.rv_favorite.apply {
            layoutManager = GridLayoutManager(activity, 2)
            adapter = MovieAdapter(mutableListOf(), activity!!)
        }
    }

    override fun onResume() {
        super.onResume()
        loadFavorite()
        println("dih resume")
    }

    private fun loadFavorite(){
        val movies = mutableListOf<Movie>()
        val cursor = activity!!.contentResolver.query(DatabaseHelper.CONTENT_URI, null, null, null, null, null)
        cursor?.let {
            it.moveToFirst()
            view?.empty_view?.visibility = View.GONE
            if(it.count > 0){
                do{
                    movies.add(Movie(id = cursor.getInt(1),
                        title = cursor.getString(2),
                        overview = cursor.getString(3),
                        backdrop_path = cursor.getString(4),
                        poster_path = cursor.getString(5),
                        vote_average = cursor.getFloat(6),
                        release_date = cursor.getString(7),
                        vote_count = cursor.getLong(8))
                    )
                    it.moveToNext()
                }while (!it.isAfterLast)
            }else{
                view?.empty_view?.visibility = View.VISIBLE
            }
            view?.rv_favorite?.adapter?.let { a->
                if (a is MovieAdapter){
                    a.setMovies(movies)
                }
            }
            it.close()
        }
    }
}