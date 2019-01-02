package com.hucet.flickr.vo

import com.google.gson.annotations.SerializedName

data class MetaPhoto(
    val page: Int,
    val pages: Int,
    @SerializedName("perpage")
    val perPage: Int,
    val total: Int,
    val photo: List<Photo>
)