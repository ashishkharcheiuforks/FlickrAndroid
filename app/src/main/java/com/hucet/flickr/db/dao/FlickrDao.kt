package com.hucet.flickr.db.dao

import android.util.SparseIntArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hucet.flickr.OpenForTesting
import com.hucet.flickr.vo.Photo
import com.hucet.flickr.vo.PhotoSearchResult
import java.util.Collections

@Dao
@OpenForTesting
abstract class FlickrDao {
    @Query("SELECT * FROM photos WHERE photo_id in (:photoIds)")
    abstract fun getPhotosByIds(photoIds: List<Long>): LiveData<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPhotos(items: List<Photo>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSearchResult(photoSearchResult: PhotoSearchResult)

    @Query("SELECT * FROM photo_search_results WHERE keyword = :keyword LIMIT 1")
    abstract fun searchResult(keyword: String): LiveData<PhotoSearchResult>

    @Query("SELECT * FROM photo_search_results WHERE keyword = :keyword LIMIT 1")
    abstract fun unitSearchResult(keyword: String): PhotoSearchResult?
}