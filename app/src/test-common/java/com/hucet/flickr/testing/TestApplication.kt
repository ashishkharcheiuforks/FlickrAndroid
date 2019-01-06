package com.hucet.flickr.testing

import com.hucet.flickr.MyApplication

class TestApplication : MyApplication() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun initLeakCanary() {
    }

    override fun initDagger() {
    }

    override fun initStetho() {
    }
}