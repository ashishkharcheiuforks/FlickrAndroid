package com.hucet.flickr.view.common.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.hucet.flickr.OpenForTesting

@OpenForTesting
class FragmentBindingAdapters constructor(val fragment: Fragment) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        Glide
            .with(fragment)
            .load(url)
            .into(imageView)
    }
}