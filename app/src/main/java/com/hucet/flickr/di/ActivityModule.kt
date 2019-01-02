package com.hucet.flickr.di

import com.hucet.flickr.di.scopes.PerActivity
import com.hucet.flickr.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [])
abstract class ActivityModule {
    @PerActivity
    @ContributesAndroidInjector(modules = [])
    internal abstract fun bindMainActivity(): MainActivity
}