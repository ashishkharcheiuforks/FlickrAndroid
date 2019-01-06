package com.hucet.flickr.utils

import com.hucet.flickr.testing.fixture.FlickrLoader
import io.kotlintest.shouldBe
import org.junit.Test

class RichEndCheckerTest {
    @Test
    fun testRichEnd() {
        val metaPhoto = FlickrLoader.Paging.first().metaPhotos
        RichEndChecker.isEnd(metaPhoto.copy(pages = 100, page = 99)) shouldBe false
        RichEndChecker.isEnd(metaPhoto.copy(pages = 100, page = 100)) shouldBe true
    }
}