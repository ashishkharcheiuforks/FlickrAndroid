package com.hucet.flickr.api

enum class FlickrContentType(val index: Int) {
    PhotoOnly(1), ScreenshotsOnly(2),
    OtherOnly(3), PhotoAndScreenshots(4),
    ScreenshotsOther(5), PhotoOther(6),
    All(7)
}