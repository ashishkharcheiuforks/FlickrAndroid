package com.hucet.flickr.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.hucet.flickr.OpenForTesting
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
    fun searchNextPhotos(keyword: String): LiveData<Resource<List<Photo>>>
    @Singleton
    @OpenForTesting
    class Impl @Inject constructor(
        private val remoteApi: FlickrApi,
        private val db: FlickrDatabase,
        private val appExecutors: AppExecutors
    ) : PhotoRepository {
        private val dao: FlickrDao = db.flickrDao()

        override fun searchPhotos(keyword: String): LiveData<Resource<List<Photo>>> {
            // 네트워크를 통해 가져온다.
            return object : NetworkBoundResource<List<Photo>, PhotoResponse>(appExecutors) {

                override fun saveCallResult(item: PhotoResponse) {
                    val photoIds = item.metaPhotos.photos.map { it.id }
                    val photoSearchResult = PhotoSearchResult(
                        keyword = keyword,
                        photoIds = photoIds,
                        next = item.nextPage
                    )
                    db.beginTransaction()
                    try {
                        dao.insertPhotos(item.metaPhotos.photos)
                        dao.insertSearchResult(photoSearchResult)
                        db.setTransactionSuccessful()
                    } finally {
                        db.endTransaction()
                    }
                }

                override fun shouldFetch(data: List<Photo>?): Boolean {
                    // 생성된 시간이 10분 초과되면 ture 반환
                    // 가져온 데이터가 없는 경우 true 반환
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

                override fun createCall() = remoteApi.getPhotos(keyword)

                override fun handleResponse(response: ApiSuccessResponse<PhotoResponse>): PhotoResponse {
                    val body = response.body
                    body.nextPage = body.metaPhotos.page + 1
                    return body
                }
            }.asLiveData()
        }

        override fun searchNextPhotos(keyword: String): LiveData<Resource<List<Photo>>> {
            // 1. cache로 부터 next page 를 가져온다.
            // 검색된 결과가 없는 경우 네트워크를 통해 가져온다.
            // 2. shouldFetch() 에서 true를 반환하면 네트워크를 통해 가져온다.
            // false를 반환하면 cache로 부터 가져온다.

            // handle responses
            // 1. 네트워크로 부터 가져온 데이터는 cache에 저장한다.
            // 2. cache에 search query를 통해 데이터를 가져온다.
            TODO("")
        }
    }
}