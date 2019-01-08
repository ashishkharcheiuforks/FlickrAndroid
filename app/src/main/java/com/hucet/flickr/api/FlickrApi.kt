package com.hucet.flickr.api

import androidx.lifecycle.LiveData
import com.hucet.flickr.BuildConfig
import com.hucet.flickr.vo.PhotoResponse
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
            const val SORT = "sort"
            const val CONTENT_TYPE = "content_type"
            const val NO_JSON_CALLBACK = "nojsoncallback"
            const val PAGE = "page"
            const val PER_PAGE = "per_page"
        }

        object QueryValue {
            const val METHOD = "flickr.photos.search"
            const val FORMAT = "json"
            const val SORT = "date-taken-desc"
            const val EXTRAS = "description, url_o, url_s, date_taken"
            const val NO_JSON_CALLBACK = 1
            const val PER_PAGE = 10
        }
    }

    @GET(SEARCH_PATH)
    fun searchPhotos(
        @Query(QueryKey.TEXT) text: String,
        @Query(QueryKey.PAGE) page: Int,
        @Query(QueryKey.PER_PAGE) perPage: Int = QueryValue.PER_PAGE,
        @Query(QueryKey.EXTRAS) extras: String = QueryValue.EXTRAS,
        @Query(QueryKey.METHOD) method: String = QueryValue.METHOD,
        @Query(QueryKey.API_KEY) apiKey: String = BuildConfig.API_KEY,
        @Query(QueryKey.FORMAT) format: String = QueryValue.FORMAT,
        @Query(QueryKey.SORT) sort: String = QueryValue.SORT,
        @Query(QueryKey.CONTENT_TYPE) contentType: Int = FlickrContentType.PhotoOnly.index,
        @Query(QueryKey.NO_JSON_CALLBACK) noJsonCallback: Int = QueryValue.NO_JSON_CALLBACK
    ): LiveData<ApiResponse<PhotoResponse>>
}