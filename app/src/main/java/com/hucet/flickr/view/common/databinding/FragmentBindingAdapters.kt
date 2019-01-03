package com.hucet.flickr.view.common.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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