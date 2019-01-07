package com.hucet.flickr.view.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import com.hucet.flickr.api.ApiResponse
import com.hucet.flickr.api.ApiSuccessResponse
import com.hucet.flickr.api.FlickrApi
import com.hucet.flickr.db.FlickrDatabase
import com.hucet.flickr.repository.PhotoRepository
import com.hucet.flickr.testing.InstantAppExecutors
import com.hucet.flickr.testing.TestApplication
import com.hucet.flickr.testing.fixture.FlickrLoader
import com.hucet.flickr.utils.createPairObserverCaptor
import com.hucet.flickr.vo.Photo
import com.hucet.flickr.vo.PhotoResponse
import com.hucet.flickr.vo.Resource
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.kotlintest.shouldBe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21], application = TestApplication::class)
class FlickrSearchViewModelRefreshTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var cacheDB: FlickrDatabase
    @Mock
    lateinit var remoteApi: FlickrApi

    private lateinit var appExecutors: InstantAppExecutors

    private lateinit var viewModel: FlickrSearchViewModel
    private lateinit var repository: PhotoRepository
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        cacheDB = FlickrDatabase.getInstanceInMemory(ApplicationProvider.getApplicationContext())
        appExecutors = InstantAppExecutors()
        repository = PhotoRepository.Impl(remoteApi, cacheDB, appExecutors)
        viewModel = FlickrSearchViewModel(repository)
    }

    @Test
    fun `refresh`() {
        val first = MutableLiveData<ApiResponse<PhotoResponse>>().apply {
            value = ApiSuccessResponse(FlickrLoader.Paging.first())
        }
        val second = MutableLiveData<ApiResponse<PhotoResponse>>().apply {
            value = ApiSuccessResponse(FlickrLoader.Paging.second())
        }
        whenever(remoteApi.searchPhotos(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(first).thenReturn(second)

        val (observer, captor) =
                createPairObserverCaptor<Resource<List<Photo>>>()
        viewModel.results.observeForever(observer)

        viewModel.search("aa")
        viewModel.refresh()
        verify(observer, times(4)).onChanged(captor.capture())

        captor.firstValue shouldBe Resource.loading(null)
        captor.secondValue.data shouldBe FlickrLoader.Paging.first().metaPhotos.photos
        captor.thirdValue shouldBe Resource.loading(null)
        captor.lastValue.data shouldBe FlickrLoader.Paging.second().metaPhotos.photos
    }
}