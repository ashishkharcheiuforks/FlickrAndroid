package com.hucet.flickr.api

import androidx.lifecycle.LiveData
import com.hucet.flickr.BuildConfig
import com.hucet.flickr.vo.MetaPhoto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    @GET("/services/rest/")
    fun getPhotos(
            @Query("text") text: String,
            @Query("extras") extras: String = "description",
            @Query("method") method: String = "flickr.photos.search",
            @Query("api_key") apiKey: String = BuildConfig.API_KEY,
            @Query("format") format: String = "json",
            @Query("nojsoncallback") noJsonCallback: Int = 1
    ): LiveData<ApiResponse<MetaPhoto>>
}
