package com.ydhnwb.moviecatalogue

import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.google.android.material.appbar.AppBarLayout
import com.ydhnwb.moviecatalogue.databases.DatabaseHelper
import com.ydhnwb.moviecatalogue.models.Movie
import com.ydhnwb.moviecatalogue.models.TvShow
import com.ydhnwb.moviecatalogue.utilities.Constants
import com.ydhnwb.moviecatalogue.utilities.VpPagerAdapter
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { finish() }
        fetch()
    }


    private fun isFavorite(id : String) : Boolean{
        val cursor = contentResolver.query(DatabaseHelper.CONTENT_URI.buildUpon().appendPath(id).build(),
            arrayOf(DatabaseHelper.COLUMN_MOVIE_ID), "${DatabaseHelper.COLUMN_MOVIE_ID} = ?",
            arrayOf(id), null, null)
        val isExists = (cursor!!.count > 0)
        cursor.close()
        if(isExists){
            btn_favorite.text = getString(R.string.btn_text_remove_favorite)
        }else{
            btn_favorite.text = getString(R.string.btn_text_add_favorite)
        }
        return isExists
    }


    private fun setOrRemoveFavorite(movie : Movie){
        btn_favorite.setOnClickListener {
            if(!isFavorite(movie.id.toString())){
                val contentValues = ContentValues()
                println("Added movie id ${movie.id} to db")
                contentValues.apply {
                    put(DatabaseHelper.COLUMN_MOVIE_ID, movie.id)
                    put(DatabaseHelper.COLUMN_MOVIE_BACKDROP, movie.backdrop_path)
                    put(DatabaseHelper.COLUMN_MOVIE_POSTER, movie.poster_path)
                    put(DatabaseHelper.COLUMN_MOVIE_TITLE, movie.title)
                    put(DatabaseHelper.COLUMN_MOVIE_OVERVIEW, movie.overview)
                    put(DatabaseHelper.COLUMN_MOVIE_RATING, movie.vote_average)
                    put(DatabaseHelper.COLUMN_MOVIE_VOTE_COUNT, movie.vote_count)
                    put(DatabaseHelper.COLUMN_MOVIE_RELEASE_DATE, movie.release_date)
                }
                contentResolver.insert(DatabaseHelper.CONTENT_URI, contentValues)
                isFavorite(movie.id.toString())
            }else{
                var uri = DatabaseHelper.CONTENT_URI
                uri = uri.buildUpon().appendPath(movie.id.toString()).build()
                contentResolver.delete(uri, null, null)
                isFavorite(movie.id.toString())
            }
        }
    }

    private fun isMovie() = intent.getBooleanExtra("IS_MOVIE", true)

    private fun fetch(){
        if(isMovie()){
            val movie = intent.getParcelableExtra<Movie>("DATA")
            movie?.let {
                detail_date.text = movie.release_date.toString()
                detail_desc.text = movie.overview
                init(movie.title.toString(), movie.vote_average!!,
                    movie.overview.toString(), movie.poster_path.toString(),
                    movie.backdrop_path.toString(), movie.vote_count ?: 0L)
                isFavorite(movie.id.toString())
                setOrRemoveFavorite(movie)
            }

        }else{
//            val tvshow = intent.getParcelableExtra<TvShow>("DATA")
//            detail_desc.text = tvshow.overview
//            init(tvshow!!.name.toString(), tvshow.voteAverage!!, tvshow.overview.toString(), tvshow.poster_path.toString(),tvshow.backdrop_path.toString(), 0L)
        }
    }


    private fun toast(message : String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    private fun init(title : String, rating : Float, desc : String, poster : String, backdrop : String,vote_count : Long) {
        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val isShow = false
            var scrollRange = -1
            if (scrollRange == -1) {
                scrollRange = appBarLayout.totalScrollRange
            }
            when {
                scrollRange + verticalOffset == 0 -> {
                    val s: String = if (supportActionBar?.title == null) " " else title
                    toolbar_layout.title = s
                }
                isShow -> toolbar_layout.title = " "
                scrollRange + verticalOffset > 0 -> toolbar_layout.title = " "
            }
        })

        detail_poster.load(Constants.API_IMAGE_ENDPOINT+"$backdrop"){
            crossfade(true)
            crossfade(1500)
        }
        detail_vp.adapter = VpPagerAdapter(supportFragmentManager, poster, title, rating, desc, vote_count)
    }
}