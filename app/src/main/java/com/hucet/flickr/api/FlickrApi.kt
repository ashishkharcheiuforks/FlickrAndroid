package com.hucet.flickr.api

import androidx.lifecycle.LiveData
import com.hucet.flickr.BuildConfig
import com.hucet.flickr.vo.MetaPhoto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    companion object {
        const val SEARCH_PATH = "/services/rest/"

        object QueryKey {
            const val TEXT = "text"
            const val EXTRAS = "extras"
            const val METHOD = "method"
            const val API_KEY = "api_key"
            const val FORMAT = "format"
            const val NO_JSON_CALLBACK = "nojsoncallback"
        }

        object QueryValue {
            const val METHOD = "flickr.photos.search"
            const val FORMAT = "json"
            const val EXTRAS = "description"
            const val NO_JSON_CALLBACK = 1
        }
    }

    @GET(SEARCH_PATH)
    fun getPhotos(
            @Query(QueryKey.TEXT) text: String,
            @Query(QueryKey.EXTRAS) extras: String = QueryValue.EXTRAS,
            @Query(QueryKey.METHOD) method: String = QueryValue.METHOD,
            @Query(QueryKey.API_KEY) apiKey: String = BuildConfig.API_KEY,
            @Query(QueryKey.FORMAT) format: String = QueryValue.FORMAT,
            @Query(QueryKey.NO_JSON_CALLBACK) noJsonCallback: Int = QueryValue.NO_JSON_CALLBACK
    ): LiveData<ApiResponse<MetaPhoto>>
}
