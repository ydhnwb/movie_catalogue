package com.ydhnwb.moviecatalogue.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    @SerializedName("id") var id : Int?,
    @SerializedName("title") var title : String?,
    @SerializedName("overview") var overview : String?,
    @SerializedName("backdrop_path") var backdrop_path : String?,
    @SerializedName("poster_path") var poster_path : String?,
    @SerializedName("vote_average") var vote_average : Float?,
    @SerializedName("release_date") var release_date : String?,
    @SerializedName("vote_count") var vote_count : Long?
) : Parcelable {
    constructor() : this(null, null, null,  null, null, 0F, null, 0L)
}