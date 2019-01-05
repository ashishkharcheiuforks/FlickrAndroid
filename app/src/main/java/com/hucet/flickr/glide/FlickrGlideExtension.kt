package com.hucet.flickr.glide

import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.request.RequestOptions
import com.hucet.flickr.R

@GlideExtension
object FlickrGlideExtension {

    @GlideOption
    @JvmStatic
    fun flickrThumb(options: RequestOptions): RequestOptions {
        return options.placeholder(R.drawable.ic_glide_placeholder).centerCrop()
    }
}