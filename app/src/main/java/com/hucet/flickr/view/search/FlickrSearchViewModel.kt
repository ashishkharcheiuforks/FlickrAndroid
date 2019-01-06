package com.hucet.flickr.view.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hucet.flickr.OpenForTesting
import com.hucet.flickr.repository.PhotoRepository
import com.hucet.flickr.utils.AbsentLiveData
import com.hucet.flickr.vo.Photo
import com.hucet.flickr.vo.Resource
import com.hucet.flickr.vo.Status
import javax.inject.Inject

@OpenForTesting
class FlickrSearchViewModel @Inject constructor(
    private val repository: PhotoRepository
) : ViewModel() {
    private val searchKeyword = MutableLiveData<String>()
    private val nextPageHandler = NextPageHandler(repository)

    val results: LiveData<Resource<List<Photo>>> = Transformations
            .switchMap(searchKeyword) { search ->
                if (search.isNullOrBlank()) {
                    AbsentLiveData.create()
                } else {
                    repository.searchPhotos(search)
                }
            }

    fun search(keyword: String) {
        if (keyword.isEmpty() || keyword == searchKeyword.value)
            return
        searchKeyword.value = keyword
    }

    fun loadNextPage() {
        searchKeyword.value?.let {
            if (it.isNotBlank()) {
                nextPageHandler.nextPage(it)
            }
        }
    }

    data class LoadMoreState(
        val isRunning: Boolean,
        val errorMessage: String?
    )

    class NextPageHandler(
        private val repository: PhotoRepository
    ) : Observer<Resource<Boolean>> {
        private var query: String? = null
        private var nextPageLiveData: LiveData<Resource<Boolean>>? = null
        private var _hasMore: Boolean = false
        val loadMoreState = MutableLiveData<LoadMoreState>()
        val hasMore
            get() = _hasMore

        fun nextPage(keyword: String) {
            if (isRunning(keyword)) {
                return
            }
            unregister()
            this.query = keyword
            nextPageLiveData = repository.searchNextPhotos(keyword)
            loadMoreState.value = LoadMoreState(
                    isRunning = true,
                    errorMessage = null
            )
            nextPageLiveData?.observeForever(this)
        }

        override fun onChanged(result: Resource<Boolean>?) {
            if (result == null)
                reset()
            else {
                when (result.status) {
                    Status.SUCCESS -> {
                        _hasMore = result.data == true
                        unregister()
                        loadMoreState.setValue(
                                LoadMoreState(
                                        isRunning = false,
                                        errorMessage = null
                                )
                        )
                    }
                    Status.ERROR -> {
                        _hasMore = true
                        unregister()
                        loadMoreState.setValue(
                                LoadMoreState(
                                        isRunning = false,
                                        errorMessage = result.message
                                )
                        )
                    }
                    Status.LOADING -> {
                        // do nothing
                    }
                }
            }
        }

        @VisibleForTesting
        fun hasActiveObservers(): Boolean = nextPageLiveData?.hasActiveObservers() == true

        private fun isRunning(keyword: String): Boolean = this.query == keyword

        private fun reset() {
            unregister()
            _hasMore = true
            loadMoreState.value = LoadMoreState(
                    isRunning = false,
                    errorMessage = null
            )
        }

        private fun unregister() {
            nextPageLiveData?.removeObserver(this)
            nextPageLiveData = null
            if (_hasMore) {
                query = null
            }
        }
    }
}