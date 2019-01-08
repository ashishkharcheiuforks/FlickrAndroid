package com.hucet.flickr.testing.fixture

import com.google.gson.Gson
import com.hucet.flickr.vo.PhotoResponse

object FlickrLoader {

    private val gson = Gson()
    private fun loadFromResources(path: String): String = this::class.java.classLoader.getResource(path).readText()

    object Paging {
        fun first(): PhotoResponse {
            val json = FlickrLoader.loadFromResources("flickr/page/first.page.json")
            return gson.fromJson(json, PhotoResponse::class.java)
        }

        fun second(): PhotoResponse {
            val json = FlickrLoader.loadFromResources("flickr/page/second.page.json")
            return gson.fromJson(json, PhotoResponse::class.java)
        }

        fun third(): PhotoResponse {
            val json = FlickrLoader.loadFromResources("flickr/page/third.page.json")
            return gson.fromJson(json, PhotoResponse::class.java)
        }
    }
}