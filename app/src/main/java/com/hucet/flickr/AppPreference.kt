package com.hucet.flickr

import android.annotation.SuppressLint
import android.content.Context

object AppPreference {
    private const val PREFS_FILE_NAME = "my_prefs"

    @SuppressLint("ApplySharedPref")
    fun saveKeyword(context: Context, keyword: String) {
        context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE).apply {
            edit().putString(ArgKey.Keyword.name, keyword).commit()
        }
    }

    fun getKeyword(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        return prefs.getString(ArgKey.Keyword.name, null)
    }
}