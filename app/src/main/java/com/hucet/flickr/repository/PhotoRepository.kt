package com.hucet.flickr.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.hucet.flickr.OpenForTesting
import com.hucet.flickr.api.ApiResponse
import com.hucet.flickr.api.ApiSuccessResponse
import com.hucet.flickr.api.FlickrApi
import com.hucet.flickr.db.FlickrDatabase
import com.hucet.flickr.db.dao.FlickrDao
import com.hucet.flickr.utils.AbsentLiveData
import com.hucet.flickr.utils.AppExecutors
import com.hucet.flickr.vo.Photo
import com.hucet.flickr.vo.PhotoResponse
import com.hucet.flickr.vo.PhotoSearchResult
import com.hucet.flickr.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

interface PhotoRepository {
    fun searchPhotos(keyword: String): LiveData<Resource<List<Photo>>>
    fun searchNextPhotos(keyword: String): LiveData<Resource<Boolean>>
    @Singleton
    @OpenForTesting
    class Impl @Inject constructor(
        private val remoteApi: FlickrApi,
        private val db: FlickrDatabase,
        private val appExecutors: AppExecutors
    ) : PhotoRepository {
        private val dao: FlickrDao = db.flickrDao()

        private fun savePhotosSearchResult(photos: List<Photo>, photoSearchResult: PhotoSearchResult) {
            db.beginTransaction()
            try {
                dao.insertPhotos(photos)
                dao.insertSearchResult(photoSearchResult)
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }

        override fun searchPhotos(keyword: String): LiveData<Resource<List<Photo>>> {
            return object : NetworkBoundResource<List<Photo>, PhotoResponse>(appExecutors) {
                override fun saveCallResult(item: PhotoResponse) {
                    val photoIds = item.metaPhotos.photos.map { it.id }
                    val photoSearchResult = PhotoSearchResult(
                        keyword = keyword,
                        photoIds = photoIds,
                        next = item.nextPage
                    )
                    savePhotosSearchResult(item.metaPhotos.photos, photoSearchResult)
                }

                override fun shouldFetch(data: List<Photo>?): Boolean {
                    return data == null
                }

                override fun loadFromDb(): LiveData<List<Photo>> {
                    return Transformations.switchMap(dao.searchResult(keyword)) { searchData ->
                        if (searchData == null) {
                            AbsentLiveData.create()
                        } else {
                            dao.getPhotosByIds(searchData.photoIds)
                        }
                    }
                }

                override fun createCall() = remoteApi.searchPhotos(keyword, 1)

                override fun handleResponse(response: ApiSuccessResponse<PhotoResponse>): PhotoResponse {
                    val body = response.body
                    body.nextPage = body.metaPhotos.page + 1
                    return body
                }
            }.asLiveData()
        }

        override fun searchNextPhotos(keyword: String): LiveData<Resource<Boolean>> {
            return object : FetchNextSearchPageTask() {
                override fun savePhotosSearchResult(items: List<Photo>, searchResult: PhotoSearchResult) {
                    savePhotosSearchResult(items, searchResult)
                }

                override fun searchResultFromDb(): LiveData<PhotoSearchResult> {
                    return dao.searchResult(keyword)
                }

                override fun createCall(page: Int): LiveData<ApiResponse<PhotoResponse>> {
                    return remoteApi.searchPhotos(keyword, page)
                }

            }.asLiveData()
        }
    }
}