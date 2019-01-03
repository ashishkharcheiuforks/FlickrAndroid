package com.hucet.flickr.vo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    val id: Long,
    val title: String,
    @SerializedName("url_o")
    val imageOrigin: String
) : Parcelable