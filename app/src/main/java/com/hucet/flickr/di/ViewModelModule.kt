package com.hucet.flickr.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hucet.flickr.view.common.ViewModelFactory
import com.hucet.flickr.view.search.FlickrSearchViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @MapKey
    annotation class ViewModelKey(val value: KClass<out ViewModel>)

    @Binds
    @IntoMap
    @ViewModelKey(FlickrSearchViewModel::class)
    abstract fun bindFlickrSearchViewModel(viewModel: FlickrSearchViewModel): ViewModel
}