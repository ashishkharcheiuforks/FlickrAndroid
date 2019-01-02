package com.hucet.flickr.repository

import androidx.lifecycle.LiveData
import com.hucet.flickr.OpenForTesting
import com.hucet.flickr.api.ApiResponse
import com.hucet.flickr.api.FlickrApi
import com.hucet.flickr.vo.MetaPhoto
import com.hucet.flickr.vo.PhotoResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class PhotoRepository @Inject constructor(
        private val api: FlickrApi
) {
    fun getPhotos(keyword: String): LiveData<ApiResponse<PhotoResponse>> = api.getPhotos(keyword)
}