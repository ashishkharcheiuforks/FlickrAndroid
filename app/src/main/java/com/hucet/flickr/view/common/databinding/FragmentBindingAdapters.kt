package com.hucet.flickr.view.common.databinding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hucet.flickr.OpenForTesting
import timber.log.Timber

@OpenForTesting
class FragmentBindingAdapters constructor(val fragment: Fragment) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        Timber.i("imageUrl: $url")
        Glide
                .with(fragment)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
    }
}