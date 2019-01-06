package com.hucet.flickr.view.common.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hucet.flickr.OpenForTesting
import com.hucet.flickr.glide.GlideApp
import com.hucet.flickr.vo.Photo
import timber.log.Timber

@OpenForTesting
class FragmentBindingAdapters constructor(val fragment: Fragment) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, photo: Photo) {
        Timber.i("imageUrl: ${photo.originImageUrl}")
        GlideApp
                .with(fragment)
                .load(photo.originImageUrl)
                .thumbnail(
                    GlideApp.with(fragment)
                            .load(photo.smallImageUrl)
                            .flickrThumb())
                .transition(DrawableTransitionOptions.withCrossFade())
                .flickrThumb()
                .into(imageView)
    }
}