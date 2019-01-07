package com.hucet.flickr.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.hucet.flickr.api.ApiEmptyResponse
import com.hucet.flickr.api.ApiErrorResponse
import com.hucet.flickr.api.ApiResponse
import com.hucet.flickr.api.ApiSuccessResponse
import com.hucet.flickr.utils.AppExecutors
import com.hucet.flickr.utils.RichEndChecker
import com.hucet.flickr.vo.Photo
import com.hucet.flickr.vo.PhotoResponse
import com.hucet.flickr.vo.PhotoSearchResult
import com.hucet.flickr.vo.Resource

/**
 * A task that reads the search result in the database and fetches the next page, if it has one.
 */
abstract class FetchNextSearchPageTask(
    appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Resource<Boolean>>()

    init {
        val searchResultDb = searchResultFromDb()
        result.addSource(searchResultDb) { searchResultLoadedFromDB ->
            result.removeSource(searchResultDb)
            if (searchResultLoadedFromDB == null) {
                result.postValue(null)
            }
            val nextPage = searchResultLoadedFromDB?.next
            if (nextPage == null) {
                result.postValue(Resource.success(false))
            } else {
                val apiResponse = createCall(nextPage)
                result.addSource(apiResponse) {
                    result.removeSource(apiResponse)
                    val response = it
                    when (response) {
                        is ApiSuccessResponse -> {
                            val ids = arrayListOf<Long>().apply {
                                addAll(searchResultLoadedFromDB.photoIds)

                                addAll(response.body.metaPhotos.photos.map { it.id })
                            }

                            val photoSearchResult = PhotoSearchResult(
                                keyword = searchResultLoadedFromDB.keyword,
                                photoIds = ids,
                                next = response.body.metaPhotos.page + 1
                            )
                            appExecutors.diskIO().execute {
                                savePhotosSearchResult(response.body.metaPhotos.photos, photoSearchResult)
                                appExecutors.mainThread().execute {
                                    result.value = Resource.success(RichEndChecker.isEnd(response.body.metaPhotos))
                                }
                            }
                        }
                        is ApiErrorResponse -> {
                            result.value = Resource.error(response.errorMessage, true)
                        }
                        is ApiEmptyResponse -> {
                            result.value = Resource.success(false)
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