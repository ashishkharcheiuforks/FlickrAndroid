package com.hucet.flickr.vo

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hucet.flickr.db.convertors.DateConverters
import com.hucet.flickr.db.convertors.PhotoIdsToListConverters
import com.hucet.flickr.vo.PhotoSearchResult.Companion.PHOTO_SEARCH_RESULT_TABLE

@Entity(
        tableName = PHOTO_SEARCH_RESULT_TABLE,
        indices = [
            Index("keyword", unique = true)
        ]
)
@TypeConverters(PhotoIdsToListConverters::class, DateConverters::class)
data class PhotoSearchResult(
    @PrimaryKey
    val keyword: String,
    val photoIds: List<Long>,
    val next: Int?
) {
    companion object {
        const val PHOTO_SEARCH_RESULT_TABLE = "photo_search_results"
    }
}