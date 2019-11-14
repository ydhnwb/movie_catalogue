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
import com.ydhnwb.moviecatalogue.models.TvShow
import com.ydhnwb.moviecatalogue.utilities.Constants
import kotlinx.android.synthetic.main.list_item_tvshow.view.*

class TvShowAdapter (private var tvShows : MutableList<TvShow>, private var context: Context) : RecyclerView.Adapter<TvShowAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_tvshow, parent, false))

    override fun getItemCount() = tvShows.size

    fun setTvShows(tvs : List<TvShow>){
        tvShows.clear()
        tvShows.addAll(tvs)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(tvShows[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(tvShow : TvShow, context: Context){
            itemView.title.text = tvShow.name
            itemView.preview_image.load("${Constants.API_IMAGE_ENDPOINT}${tvShow.poster_path}")
            itemView.setOnClickListener {
                context.startActivity(Intent(context, DetailActivity::class.java).apply {
                    putExtra("IS_MOVIE", false)
                    putExtra("DATA", tvShow)
                })
            }
        }
    }
}