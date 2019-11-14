package com.ydhnwb.moviecatalogue.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ydhnwb.moviecatalogue.models.TvShow
import com.ydhnwb.moviecatalogue.utilities.Constants
import com.ydhnwb.moviecatalogue.utilities.SingleLiveEvent
import com.ydhnwb.moviecatalogue.webservices.APIConfig
import com.ydhnwb.moviecatalogue.webservices.BaseListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowViewModel : ViewModel(){
    private var state : SingleLiveEvent<TvShowState> = SingleLiveEvent()
    private var tvShows = MutableLiveData<List<TvShow>>()
    private var api = APIConfig.APIService()

    fun getTvShow() {
        state.value = TvShowState.IsLoading(true)
        api.tv_show(Constants.API_KEY_MOVIE).enqueue(object : Callback<BaseListResponse<TvShow>>{
            override fun onFailure(call: Call<BaseListResponse<TvShow>>, t: Throwable) {
                state.value = TvShowState.ShowToast(t.message.toString())
                state.value = TvShowState.IsLoading(false)
            }

            override fun onResponse(call: Call<BaseListResponse<TvShow>>, response: Response<BaseListResponse<TvShow>>) {
                if(response.isSuccessful){
                    val body = response.body()
                    body?.let {
                        tvShows.postValue(it.results)
                    }
                }else{
                    state.value = TvShowState.ShowToast("Terjadi kesalahan")
                }
                state.value = TvShowState.IsLoading(false)
            }
        })
    }

    fun state() = state
    fun tvShows() = tvShows
}

sealed class TvShowState {
    data class ShowToast(var message : String) : TvShowState()
    data class IsLoading(var state : Boolean = false) : TvShowState()
}