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
import com.hucet.flickr.vo.Photo
import com.hucet.flickr.vo.PhotoResponse
import com.hucet.flickr.vo.Resource
import com.hucet.flickr.vo.Status
import com.nhaarman.mockito_kotlin.KArgumentCaptor
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
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
import retrofit2.Call

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

        val expectResponse = FlickrLoader.Paging.first()
        // given
        val remoteLivedata = MutableLiveData<ApiResponse<PhotoResponse>>().apply {
            value = ApiSuccessResponse(expectResponse)
        }
        remoteApi.stubSearchPhotos(null, remoteLivedata)

        // when
        repository.searchPhotos("test").observeForever(observer)
        verify(observer, times(2)).onChanged(captor.capture())

        // then
        captor.firstValue shouldBe Resource.loading(null)
        captor.secondValue.status shouldBe Status.SUCCESS
        captor.secondValue.data?.size shouldBe expectResponse.metaPhotos.photos.size
        captor.secondValue.data!! shouldContainAll expectResponse.metaPhotos.photos
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
    fun `aaa`() {
        val (observer, captor) =
                createPairObserverCaptor<Resource<List<Photo>>>()
        val (nextObserver, nextCaptor) =
                createPairObserverCaptor<Resource<Boolean>>()

        // given
        val expectResponse = FlickrLoader.Paging.first()
        val remoteLivedata = MutableLiveData<ApiResponse<PhotoResponse>>().apply {
            value = ApiSuccessResponse(expectResponse)
        }
        with(remoteApi) {
            stubSearchPhotos(null, remoteLivedata)
        }
        repository.searchPhotos("test").observeForever(observer)
        verify(observer, times(2)).onChanged(captor.capture())

        repository.searchNextPhotos("test").observeForever(nextObserver)
        println(captor.allValues)
    }
}

inline fun <reified T> createPairObserverCaptor(): Pair<Observer<T>, KArgumentCaptor<T>> {
    val captor = KArgumentCaptor(ArgumentCaptor.forClass(T::class.java), T::class)
    val observer = mock<Observer<T>>()
    return observer to captor
}

fun FlickrApi.stubSearchPhotos(keyword: String?, expects: LiveData<ApiResponse<PhotoResponse>>, page: Int = 1) {
    val secondLiveData = MutableLiveData<ApiResponse<PhotoResponse>>().apply {
        value = ApiSuccessResponse(FlickrLoader.Paging.second())
    }
    val thirdLiveData = MutableLiveData<ApiResponse<PhotoResponse>>().apply {
        value = ApiSuccessResponse(FlickrLoader.Paging.third())
    }

    doAnswer {
        val page = it.arguments[1]
        when (page) {
            1 -> {
                expects
            }
            2 -> {
                secondLiveData
            }
            else -> {
                thirdLiveData
            }

        }
        expects
    }.`when`(this).searchPhotos(
        keyword ?: any(), eq(page) ?: any(),
        any(), any(), any(), any(), any(), any(), any()
    )
}
