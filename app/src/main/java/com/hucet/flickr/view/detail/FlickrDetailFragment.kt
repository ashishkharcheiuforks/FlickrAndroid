package com.hucet.flickr.view.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hucet.flickr.ArgKey
import com.hucet.flickr.R
import com.hucet.flickr.databinding.FragmentFlickrDetailBinding
import com.hucet.flickr.utils.autoCleared
import com.hucet.flickr.view.common.databinding.FragmentDataBindingComponent
import com.hucet.flickr.vo.Photo

class FlickrDetailFragment : Fragment() {
    companion object {
        fun newInstance(photo: Photo): FlickrDetailFragment {
            return FlickrDetailFragment().apply {
                arguments = bundleOf(
                        ArgKey.Photo.name to photo
                )
            }
        }
    }

    private val photo by lazy {
        arguments?.getParcelable(ArgKey.Photo.name) as Photo
    }
    var binding by autoCleared<FragmentFlickrDetailBinding>()

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_flickr_detail,
                container,
                false,
                dataBindingComponent
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                parentFragment?.startPostponedEnterTransition()
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                parentFragment?.startPostponedEnterTransition()
                return false
            }
        }
        Glide
                .with(this)
                .load(photo.imageOrigin)
                .listener(listener)
                .into(binding.detailImageView)
    }
}