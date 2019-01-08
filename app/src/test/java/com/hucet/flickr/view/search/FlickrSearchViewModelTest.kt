package com.hucet.flickr.view.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.hucet.flickr.repository.PhotoRepository
import com.hucet.flickr.testing.TestApplication
import com.hucet.flickr.utils.createPairObserverCaptor
import com.hucet.flickr.vo.Photo
import com.hucet.flickr.vo.Resource
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21], application = TestApplication::class)
class FlickrSearchViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: FlickrSearchViewModel

    private lateinit var repository: PhotoRepository
    @Before
    fun setUp() {
        repository = mock()
        viewModel = FlickrSearchViewModel(repository)
    }

    @Test
    fun `검색어가 비어있으면 data를 가져오지 않음`() {
        viewModel.search("")
        val (observer, captor) =
                createPairObserverCaptor<Resource<List<Photo>>>()
        viewModel.results.observeForever(observer)
        verify(observer, never()).onChanged(captor.capture())
    }

    @Test
    fun `이전 검색어와 동일하면 data를 가져오지 않음`() {
        val liveData = MutableLiveData<Resource<List<Photo>>>()
        liveData.value = Resource.loading(null)
        doReturn(liveData).`when`(repository).searchPhotos(any())

        val keyword = "tt"
        viewModel.search(keyword)
        val (observer, captor) =
                createPairObserverCaptor<Resource<List<Photo>>>()
        viewModel.results.observeForever(observer)
        verify(observer, times(1)).onChanged(captor.capture())
        reset(observer)
        viewModel.search(keyword)
        verify(observer, never()).onChanged(captor.capture())
    }
}