package com.hucet.flickr.di

import com.hucet.flickr.BuildConfig
import com.hucet.flickr.api.FlickrApi
import com.hucet.flickr.utils.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [])
class FlickrModule {

    @Singleton
    @Provides
    fun provideServiceOkHttpClient(): OkHttpClient.Builder {
        val builer = OkHttpClient.Builder()
                .addLoggingIntercept()
                .setTimeouts()
        return builer
    }

    @Provides
    @Singleton
    fun provideFilckrApi(okHttpClient: OkHttpClient.Builder): FlickrApi = Retrofit.Builder()
            .client(okHttpClient.build())
            .baseUrl(BuildConfig.FLICKR_BASE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(FlickrApi::class.java)

    private fun OkHttpClient.Builder.addLoggingIntercept(
        level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY
    ): OkHttpClient.Builder {
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = level
            addInterceptor(logging)
        }
        return this
    }

    private fun OkHttpClient.Builder.setTimeouts(milliSecond: Long = BuildConfig.NETWORK_TIMEOUT_MILLIS): OkHttpClient.Builder {
        connectTimeout(milliSecond, TimeUnit.MILLISECONDS)
        readTimeout(milliSecond, TimeUnit.MILLISECONDS)
        writeTimeout(milliSecond, TimeUnit.MILLISECONDS)
        return this
    }
}