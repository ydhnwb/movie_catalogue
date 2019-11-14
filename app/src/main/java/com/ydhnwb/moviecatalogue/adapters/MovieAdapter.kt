package com.ydhnwb.moviecatalogue.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.ydhnwb.moviecatalogue.DetailActivity
import com.ydhnwb.moviecatalogue.R
import com.ydhnwb.moviecatalogue.models.Movie
import com.ydhnwb.moviecatalogue.utilities.Constants
import kotlinx.android.synthetic.main.list_item_movie.view.*

class MovieAdapter (private var movies : MutableList<Movie>, private val context : Context) : RecyclerView.Adapter<MovieAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_movie, parent, false))

    fun setMovies(mvs : List<Movie>){
        movies.clear()
        movies.addAll(mvs)
        notifyDataSetChanged()
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(movie : Movie, context: Context){
            itemView.title.text = movie.title
            itemView.preview_image.load("${Constants.API_IMAGE_ENDPOINT}${movie.poster_path}")
            itemView.setOnClickListener {
                context.startActivity(Intent(context, DetailActivity::class.java).apply {
                    putExtra("IS_MOVIE", true)
                    putExtra("DATA", movie)
                })
            }
        }
    }
}