package com.hucet.flickr.vo

import androidx.room.Entity
import androidx.room.TypeConverters
import com.hucet.flickr.db.PhotoIdsToListConverters

@Entity(primaryKeys = ["keyword"])
@TypeConverters(PhotoIdsToListConverters::class)
data class PhotoSearchResult(
    val keyword: String,
    val photoIds: List<Int>,
    val next: Int?
)
