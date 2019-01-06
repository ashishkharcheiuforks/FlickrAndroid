package com.hucet.flickr.db.dao

import androidx.room.Dao
import com.hucet.flickr.OpenForTesting
import com.hucet.flickr.vo.Photo

@Dao
@OpenForTesting
abstract class FlickrDao : BaseDao<Photo> {
}