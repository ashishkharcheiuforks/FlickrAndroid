package com.hucet.flickr.testing.fixture

import com.hucet.flickr.vo.PhotoResponse

data class ExpectItem(
    val test: PhotoResponse,
    val expect: PhotoResponse
)