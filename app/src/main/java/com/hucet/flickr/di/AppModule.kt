package com.hucet.flickr.di

import dagger.Module

@Module(includes = [ActivityModule::class, FlickrModule::class])
class AppModule {

}