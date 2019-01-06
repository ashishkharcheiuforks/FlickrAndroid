package com.hucet.flickr.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.hucet.flickr.api.ApiResponse
import com.hucet.flickr.api.ApiSuccessResponse
import com.hucet.flickr.api.FlickrApi
import com.hucet.flickr.db.FlickrDatabase
import com.hucet.flickr.testing.InstantAppExecutors
import com.hucet.flickr.testing.TestApplication
import com.hucet.flickr.testing.TestException
import com.hucet.flickr.testing.fixture.FlickrLoader
import com.hucet.flickr.vo.*
import com.nhaarman.mockito_kotlin.*
import io.kotlintest.matchers.collections.shouldContainAll
import io.kotlintest.shouldBe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21], application = TestApplication::class)
class PhotoRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: PhotoRepository

    private lateinit var cacheDB: FlickrDatabase
    @Mock
    lateinit var remoteApi: FlickrApi

    private lateinit var appExecutors: InstantAppExecutors
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        cacheDB = FlickrDatabase.getInstanceInMemory(ApplicationProvider.getApplicationContext())
        appExecutors = InstantAppExecutors()
        repository = PhotoRepository.Impl(remoteApi, cacheDB, appExecutors)
    }

    @Test
    fun `Then equals expect items when searchPhotos`() {
        val (observer, captor) =
                createPairObserverCaptor<Resource<List<Photo>>>()

        // given
        val expects = remoteApi.stubSuccessSearchPhotos(null).first()

        // when
        repository.searchPhotos("test").observeForever(observer)
        verify(observer, times(2)).onChanged(captor.capture())

        // then
        captor.firstValue shouldBe Resource.loading(null)
        captor.secondValue shouldBe Resource.success(expects.metaPhotos.photos)
    }

    @Test
    fun `Then thrown a exception when searchPhotos`() {
        val (observer, captor) =
                createPairObserverCaptor<Resource<List<Photo>>>()
        // given
        val remoteLivedata = MutableLiveData<ApiResponse<PhotoResponse>>().apply {
            value = ApiResponse.create(TestException("test"))
        }
        remoteApi.stubSearchPhotos(null, remoteLivedata)

        repository.searchPhotos("test").observeForever(observer)
        verify(observer, times(2)).onChanged(captor.capture())

        captor.firstValue shouldBe Resource.loading(null)
        captor.secondValue.message shouldBe "test"
    }

    @Test
    fun `Then increased a page when searchNextPhotos`() {
        val keyword = "test"
        val (searchResultObserver, searchResultCaptor) =
                createPairObserverCaptor<PhotoSearchResult>()
        // given
        val expects = remoteApi.stubSuccessSearchPhotos(null)

        cacheDB.flickrDao().searchResult(keyword).observeForever(searchResultObserver)

        repository.searchPhotos(keyword).observeForever { }
        repository.searchNextPhotos(keyword).observeForever { }

        verify(searchResultObserver, times(3)).onChanged(searchResultCaptor.capture())
        searchResultCaptor.firstValue shouldBe null
        searchResultCaptor.secondValue shouldBe PhotoSearchResult(keyword, FlickrLoader.Paging.first().metaPhotos.photos.map { it.id }, 2)

        val secondPhotoIds = expects[0].metaPhotos.photos.map { it.id } + expects[1].metaPhotos.photos.map { it.id }
        searchResultCaptor.thirdValue shouldBe PhotoSearchResult(keyword, secondPhotoIds, 3)
    }

    @Test
    fun `searchNextPhotos를 호출되면 Observer 가 통지 되어야 함`() {
        val keyword = "test"

        val expects = remoteApi.stubSuccessSearchPhotos(null)

        // given
        val (observer, captor) =
                createPairObserverCaptor<Resource<List<Photo>>>()
        repository.searchPhotos(keyword).observeForever(observer)
        repository.searchNextPhotos(keyword).observeForever { }

        verify(observer, times(3)).onChanged(captor.capture())
        captor.firstValue shouldBe Resource.loading(null)
        captor.secondValue shouldBe Resource.success(expects[0].metaPhotos.photos)
        captor.lastValue shouldBe Resource.success(expects[0].metaPhotos.photos + expects[1].metaPhotos.photos)
    }
}

inline fun <reified T> createPairObserverCaptor(): Pair<Observer<T>, KArgumentCaptor<T>> {
    val captor = KArgumentCaptor(ArgumentCaptor.forClass(T::class.java), T::class)
    val observer = mock<Observer<T>>()
    return observer to captor
}

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
            any(), any(), any(), any(), any(), any(), any()
    )
    return listOf(firstData, secondData, thirdData)
}


fun FlickrApi.stubSearchPhotos(keyword: String?, expects: LiveData<ApiResponse<PhotoResponse>>) {
    doReturn(expects).`when`(this).searchPhotos(
            keyword ?: any(), any(),
            any(), any(), any(), any(), any(), any(), any()
    )
}
