package com.hucet.flickr.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hucet.flickr.db.dao.FlickrDao
import com.hucet.flickr.vo.Photo
import com.hucet.flickr.vo.PhotoSearchResult

@Database(
    entities = [
        Photo::class,
        PhotoSearchResult::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FlickrDatabase : RoomDatabase() {

    abstract fun flickrDao(): FlickrDao

    companion object {

        private var INSTANCE: FlickrDatabase? = null

        @Synchronized
        fun getInstance(context: Context): FlickrDatabase {
            if (INSTANCE == null) {
                INSTANCE = buildDatabase(context)
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        @Synchronized
        fun getInstanceInMemory(context: Context): FlickrDatabase {
            INSTANCE = Room.inMemoryDatabaseBuilder(context.applicationContext, FlickrDatabase::class.java)
                .allowMainThreadQueries()
                .populate()
                .build()
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): FlickrDatabase {
            return Room.databaseBuilder(
                context,
                FlickrDatabase::class.java, "flickr12"
            )
                .populate()
                .build()
        }
    }
}

private fun <T : RoomDatabase> RoomDatabase.Builder<T>.populate(): RoomDatabase.Builder<T> {
    return this.addCallback(object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
        }
    })
}