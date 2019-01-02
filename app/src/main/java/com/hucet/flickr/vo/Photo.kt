package com.hucet.flickr.vo

import com.google.gson.annotations.SerializedName

data class Photo(
        val id: Long,
        val title: String,
        @SerializedName("url_c")
        val urlSmall: String
)