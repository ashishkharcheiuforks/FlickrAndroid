package com.hucet.flickr.di.fragments

import com.hucet.flickr.view.search.FlickrSearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FlickrSearchFragmentModule {
    @ContributesAndroidInjector
    abstract fun bindFlickrSearchFragment(): FlickrSearchFragment
}