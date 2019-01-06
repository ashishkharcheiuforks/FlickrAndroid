package com.hucet.flickr.view.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hucet.flickr.repository.PhotoRepository
import com.hucet.flickr.testing.TestApplication
import com.hucet.flickr.vo.Resource
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.kotlintest.shouldBe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21], application = TestApplication::class)
class NextPageHandlerTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var nextPageHandler: FlickrSearchViewModel.NextPageHandler
    private lateinit var repository: PhotoRepository
    @Before
    fun setUp() {
        repository = mock()
        nextPageHandler = FlickrSearchViewModel.NextPageHandler(repository)
    }

    @Test
    fun `hasMore Test Cases`() {
        assertHasMore(Resource.success(null), false)
        assertHasMore(Resource.success(true), true)
        assertHasMore(Resource.error("error", null), true)
    }

    private fun assertHasMore(onChangedValue: Resource<Boolean>, expect: Boolean) {
        val liveData = MutableLiveData<Resource<Boolean>>()
        repository.stubNextPage(liveData)
        nextPageHandler.nextPage("tt")
        nextPageHandler.hasActiveObservers() shouldBe true
        nextPageHandler.onChanged(onChangedValue)
        nextPageHandler.hasMore shouldBe expect
        nextPageHandler.hasActiveObservers() shouldBe false
    }
}

private fun PhotoRepository.stubNextPage(expect: LiveData<Resource<Boolean>>) {
    doReturn(expect).`when`(this).searchNextPhotos(any())
}