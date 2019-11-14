package com.ydhnwb.moviecatalogue.webservices

import com.google.gson.annotations.SerializedName
import com.ydhnwb.moviecatalogue.models.Movie
import com.ydhnwb.moviecatalogue.models.TvShow
import com.ydhnwb.moviecatalogue.utilities.Constants
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

class APIConfig {
        companion object {
            private var retrofit: Retrofit? = null
            private var okHttpClient = OkHttpClient.Builder().apply {
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
            }.build()

            fun APIService(): APIService = getClient().create(APIService::class.java)

            private fun getClient(): Retrofit {
                return if (retrofit == null) {
                    retrofit = Retrofit.Builder().apply {
                        baseUrl(Constants.API_ENDPOINT)
                        client(okHttpClient).addConverterFactory(GsonConverterFactory.create())
                    }.build()
                    retrofit!!
                } else {
                    retrofit!!
                }
            }
        }
}

interface APIService {
    @GET("movie/now_playing")
    fun now_playing(@Query("api_key") apiKey : String) : Call<BaseListResponse<Movie>>

    @GET("movie/upcoming")
    fun upcoming(@Query("api_key") apiKey : String) : Call<BaseListResponse<Movie>>

    @GET("search/movie")
    fun searchMovie(@Query("api_key") apiKey : String, @Query("query") query : String) : Call<BaseListResponse<Movie>>

    @GET("tv/top_rated")
    fun tv_show(@Query("api_key") apiKey : String) : Call<BaseListResponse<TvShow>>
}


data class BaseResponse<T>(
    @SerializedName("page") var page : Int?,
    @SerializedName("total_results") var total_results : Int?,
    @SerializedName("total_pages") var total_pages : Int?,
    @SerializedName("results") var results : T?
) {
    constructor() : this(null, null, null, null)
}


data class BaseListResponse <T>(
    @SerializedName("page") var page : Int?,
    @SerializedName("total_results") var total_results : Int?,
    @SerializedName("total_pages") var total_pages : Int?,
    @SerializedName("results") var results : List<T>?
){
    constructor() : this(null, null, null, null)
}
