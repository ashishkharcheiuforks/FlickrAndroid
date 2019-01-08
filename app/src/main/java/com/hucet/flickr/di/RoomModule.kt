package com.hucet.flickr.di

import android.app.Application
import com.hucet.flickr.db.FlickrDatabase
import com.hucet.flickr.db.dao.FlickrDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {
    @Singleton
    @Provides
    internal fun providesFlickrDatabase(application: Application): FlickrDatabase {
        return FlickrDatabase.getInstance(application)
    }

    @Singleton
    @Provides
    internal fun providesFlickrDao(db: FlickrDatabase): FlickrDao {
        return db.flickrDao()
    }
}