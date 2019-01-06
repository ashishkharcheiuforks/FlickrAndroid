package com.hucet.flickr.utils

import com.hucet.flickr.vo.MetaPhoto

object RichEndChecker {
    fun isEnd(metaPhoto: MetaPhoto): Boolean {
        return metaPhoto.page >= metaPhoto.pages
    }
}