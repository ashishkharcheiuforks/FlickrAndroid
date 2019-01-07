package com.hucet.flickr.db.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.hucet.flickr.db.FlickrDatabase
import com.hucet.flickr.testing.TestApplication
import com.hucet.flickr.testing.fixture.FlickrLoader
import com.hucet.flickr.vo.Photo
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import io.kotlintest.matchers.collections.shouldContainAll
import io.kotlintest.shouldBe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21], application = TestApplication::class)
class FlickrDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: FlickrDatabase

    lateinit var dao: FlickrDao
    @Before
    fun setUp() {
        db = FlickrDatabase.getInstanceInMemory(ApplicationProvider.getApplicationContext())
        dao = db.flickrDao()
    }

    @Test
    fun `Then equals expect items when getPhotosByIds`() {
        val observer = mock<Observer<List<Photo>>>()
        val captor = argumentCaptor<List<Photo>>()
        val photos = FlickrLoader.Paging.first().metaPhotos.photos
        dao.insertPhotos(photos)
        dao.searchPhotosByIds(photos.map { it.id }).observeForever(observer)

        verify(observer).onChanged(captor.capture())

        captor.allValues.size shouldBe 1
        captor.firstValue shouldContainAll photos
        verifyNoMoreInteractions(observer)
    }
}