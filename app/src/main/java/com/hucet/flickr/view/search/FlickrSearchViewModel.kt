package com.hucet.flickr.view.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hucet.flickr.OpenForTesting
import com.hucet.flickr.api.ApiResponse
import com.hucet.flickr.repository.PhotoRepository
import com.hucet.flickr.utils.AbsentLiveData
import com.hucet.flickr.vo.PhotoResponse
import javax.inject.Inject

@OpenForTesting
class FlickrSearchViewModel @Inject constructor(
    private val repository: PhotoRepository
) : ViewModel() {
    private val searchKeyword = MutableLiveData<String>()

    val results: LiveData<ApiResponse<PhotoResponse>> = Transformations
            .switchMap(searchKeyword) { search ->
                if (search.isNullOrBlank()) {
                    AbsentLiveData.create()
                } else {
                    repository.getPhotos(search)
                }
            }

    fun search(keyword: String) {
        if (keyword.isEmpty() || keyword == searchKeyword.value)
            return
        searchKeyword.value = keyword
    }
}