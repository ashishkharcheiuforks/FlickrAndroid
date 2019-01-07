package com.hucet.flickr.utils

import android.text.Html
import android.text.Spanned

object HTMLHelper {
    fun fromHtml(html: String): Spanned {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        else
            Html.fromHtml(html)
    }
}