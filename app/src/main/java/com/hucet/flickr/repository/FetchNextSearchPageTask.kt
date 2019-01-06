package com.hucet.flickr.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.hucet.flickr.api.ApiEmptyResponse
import com.hucet.flickr.api.ApiErrorResponse
import com.hucet.flickr.api.ApiResponse
import com.hucet.flickr.api.ApiSuccessResponse
import com.hucet.flickr.vo.Photo
import com.hucet.flickr.vo.PhotoResponse
import com.hucet.flickr.vo.PhotoSearchResult
import com.hucet.flickr.vo.Resource

/**
 * A task that reads the search result in the database and fetches the next page, if it has one.
 */
abstract class FetchNextSearchPageTask {
    private val result = MediatorLiveData<Resource<Boolean>>()

    init {
        val searchResultDb = searchResultFromDb()
        result.addSource(searchResultDb) { searchResultLoadedFromDB ->
            result.removeSource(searchResultDb)
            val current = searchResultLoadedFromDB
            if (current == null) {
                result.postValue(null)
            }
            val nextPage = current?.next
            if (nextPage == null) {
                result.postValue(Resource.success(false))
            } else {
                val apiResponse = createCall(nextPage)
                result.addSource(apiResponse) {
                    result.removeSource(apiResponse)
                    val response = it
                    when (response) {
                        is ApiSuccessResponse -> {
                            val ids = arrayListOf<Long>()
                            ids.addAll(current.photoIds)

                            ids.addAll(response.body.metaPhotos.photos.map { it.id })

                            val photoSearchResult = PhotoSearchResult(
                                keyword = current.keyword,
                                photoIds = ids,
                                next = response.body.metaPhotos.page + 1
                            )
                            savePhotosSearchResult(response.body.metaPhotos.photos, photoSearchResult)
                        }
                        is ApiErrorResponse -> {
                        }
                        is ApiEmptyResponse -> {
                        }
                    }
                }
            }
        }
    }

    fun asLiveData() = result as LiveData<Resource<Boolean>>

    @WorkerThread
    protected abstract fun savePhotosSearchResult(items: List<Photo>, searchResult: PhotoSearchResult)

    @MainThread
    protected abstract fun searchResultFromDb(): LiveData<PhotoSearchResult>

    @MainThread
    protected abstract fun createCall(page: Int): LiveData<ApiResponse<PhotoResponse>>
}