package com.ydhnwb.moviecatalogue.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ydhnwb.moviecatalogue.models.Movie
import com.ydhnwb.moviecatalogue.utilities.Constants
import com.ydhnwb.moviecatalogue.utilities.SingleLiveEvent
import com.ydhnwb.moviecatalogue.webservices.APIConfig
import com.ydhnwb.moviecatalogue.webservices.BaseListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel(){
    private var state : SingleLiveEvent<MovieState> = SingleLiveEvent()
    private var nowPlayingMovies = MutableLiveData<List<Movie>>()
    private var searchResultMovies = MutableLiveData<List<Movie>>()
    private var upcomingMovies = MutableLiveData<List<Movie>>()
    private var api = APIConfig.APIService()

    fun getNowPlayingMovies() {
        state.value = MovieState.IsLoading(true)
        api.now_playing(Constants.API_KEY_MOVIE).enqueue(object : Callback<BaseListResponse<Movie>>{
            override fun onFailure(call: Call<BaseListResponse<Movie>>, t: Throwable) {
                state.value = MovieState.ShowToast(t.message.toString())
                state.value = MovieState.IsLoading(false)
            }

            override fun onResponse(call: Call<BaseListResponse<Movie>>, response: Response<BaseListResponse<Movie>>) {
                if(response.isSuccessful){
                    val body = response.body()
                    body?.let {
                        nowPlayingMovies.postValue(it.results as List<Movie>)
                    }
                }else{
                    state.value = MovieState.ShowToast("Terjadi kesalahan")
                }
                state.value = MovieState.IsLoading(false)
            }
        })
    }

    fun searchMovies(query : String) {
        state.value = MovieState.IsLoading(true)
        api.searchMovie(Constants.API_KEY_MOVIE, query).enqueue(object : Callback<BaseListResponse<Movie>>{
            override fun onFailure(call: Call<BaseListResponse<Movie>>, t: Throwable) {
                state.value = MovieState.ShowToast(t.message.toString())
                state.value = MovieState.IsLoading(false)
            }

            override fun onResponse(call: Call<BaseListResponse<Movie>>, response: Response<BaseListResponse<Movie>>) {
                if(response.isSuccessful){
                    val body = response.body()
                    body?.let {
                        searchResultMovies.postValue(it.results as List<Movie>)
                    }
                }else{
                    state.value = MovieState.ShowToast("Terjadi kesalahan")
                }
                state.value = MovieState.IsLoading(false)
            }
        })
    }

    fun getUpcomingMovies() {
        state.value = MovieState.IsLoading(true)
        api.upcoming(Constants.API_KEY_MOVIE).enqueue(object : Callback<BaseListResponse<Movie>>{
            override fun onFailure(call: Call<BaseListResponse<Movie>>, t: Throwable) {
                state.value = MovieState.ShowToast(t.message.toString())
                state.value = MovieState.IsLoading(false)
            }

            override fun onResponse(call: Call<BaseListResponse<Movie>>, response: Response<BaseListResponse<Movie>>) {
                if(response.isSuccessful){
                    val body = response.body()
                    body?.let {
                        upcomingMovies.postValue(it.results as List<Movie>)
                    }
                }else{
                    state.value = MovieState.ShowToast("Terjadi kesalahan")
                }
                state.value = MovieState.IsLoading(false)
            }
        })
    }


    fun nowPlayingMovies()= nowPlayingMovies
    fun upcomingMovies() = upcomingMovies
    fun searchResults() = searchResultMovies
    fun state() = state
}

sealed class MovieState{
    data class ShowToast(var message : String) : MovieState()
    data class IsLoading(var state : Boolean = false) : MovieState()
}