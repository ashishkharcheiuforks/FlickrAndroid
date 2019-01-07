package com.hucet.flickr.di

import com.hucet.flickr.api.FlickrApi
import com.hucet.flickr.db.FlickrDatabase
import com.hucet.flickr.repository.PhotoRepository
import com.hucet.flickr.utils.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provicesPhotoRepository(api: FlickrApi, db: FlickrDatabase, appExecutors: AppExecutors): PhotoRepository {
        return PhotoRepository.Impl(api, db, appExecutors)
    }
}