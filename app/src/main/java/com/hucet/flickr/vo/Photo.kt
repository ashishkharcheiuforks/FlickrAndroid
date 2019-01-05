package com.hucet.flickr.vo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    val id: Long,
    val title: String,
    @SerializedName("description")
    private val _description: Description,
    @SerializedName("url_o")
    val originImageUrl: String,
    @SerializedName("url_s")
    val smallImageUrl: String
) : Parcelable {
    val description: String
        get() {
            return if (_description.content.isEmpty()) title
            else _description.content
        }
}

@Parcelize
data class Description(
    @SerializedName("_content")
    val content: String
) : Parcelable