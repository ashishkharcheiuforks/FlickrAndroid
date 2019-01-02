package com.hucet.flickr.di

import com.hucet.flickr.api.FlickrApi
import com.hucet.flickr.repository.PhotoRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provicesPhotoRepository(api: FlickrApi): PhotoRepository = PhotoRepository(api)
}