package com.hucet.flickr.di

import com.hucet.flickr.di.fragments.FragmentBuildersModule
import com.hucet.flickr.di.scopes.PerActivity
import com.hucet.flickr.view.search.FlickrSearchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [])
abstract class ActivityModule {
    @PerActivity
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    internal abstract fun bindFlickrSearchActivity(): FlickrSearchActivity
}