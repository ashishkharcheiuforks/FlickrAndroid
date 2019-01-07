package com.hucet.flickr.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hucet.flickr.api.ApiResponse
import com.hucet.flickr.api.ApiSuccessResponse
import com.hucet.flickr.api.FlickrApi
import com.hucet.flickr.testing.fixture.FlickrLoader
import com.hucet.flickr.vo.PhotoResponse
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.doReturn

fun FlickrApi.stubSuccessSearchPhotos(keyword: String?): List<PhotoResponse> {
    val firstData = FlickrLoader.Paging.first()
    val firstLiveData = MutableLiveData<ApiResponse<PhotoResponse>>().apply {
        value = ApiSuccessResponse(firstData)
    }
    val secondData = FlickrLoader.Paging.second()
    val secondLiveData = MutableLiveData<ApiResponse<PhotoResponse>>().apply {
        value = ApiSuccessResponse(secondData)
    }
    val thirdData = FlickrLoader.Paging.third()
    val thirdLiveData = MutableLiveData<ApiResponse<PhotoResponse>>().apply {
        value = ApiSuccessResponse(thirdData)
    }

    doAnswer {
        val page = it.arguments[1]

        when (page) {
            1 -> {
                firstLiveData
            }
            2 -> {
                secondLiveData
            }
            else -> {
                thirdLiveData
            }
        }
    }.`when`(this).searchPhotos(
            keyword ?: any(), any(),
            any(), any(), any(), any(), any(), any(), any(), any()
    )
    return listOf(firstData, secondData, thirdData)
}

fun FlickrApi.stubSearchPhotos(keyword: String?, expects: LiveData<ApiResponse<PhotoResponse>>) {
    doReturn(expects).`when`(this).searchPhotos(
            keyword ?: any(), any(), any(), any(), any(), any(), any(), any(), any(), any()
    )
}