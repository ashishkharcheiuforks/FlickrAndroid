package com.hucet.flickr.vo

import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("photos")
    val metaPhotos: MetaPhoto
)