package com.ydhnwb.moviecatalogue.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvShow(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("name") var name : String? = null,
    @SerializedName("overview") var overview : String? = null,
    @SerializedName("backdrop_path")  var backdrop_path: String? = null,
    @SerializedName("vote_average") var voteAverage: Float? = 0F,
    @SerializedName("poster_path") var poster_path : String? = null
) : Parcelable