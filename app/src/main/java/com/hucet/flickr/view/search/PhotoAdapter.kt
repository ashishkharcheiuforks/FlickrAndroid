package com.hucet.flickr.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.hucet.flickr.R
import com.hucet.flickr.databinding.PhotoItemBinding
import com.hucet.flickr.utils.AppExecutors
import com.hucet.flickr.view.common.adapter.DataBoundListAdapter
import com.hucet.flickr.vo.Photo

typealias ITEM_PHOTO = Photo

class PhotoAdapter constructor(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((ITEM_PHOTO) -> Unit)?
) : DataBoundListAdapter<ITEM_PHOTO, PhotoItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<ITEM_PHOTO>() {
            override fun areItemsTheSame(oldItem: ITEM_PHOTO, newItem: ITEM_PHOTO): Boolean {
                return oldItem == newItem && oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ITEM_PHOTO, newItem: ITEM_PHOTO): Boolean {
//                TODO
                return oldItem == newItem && oldItem == newItem
            }
        }) {

    override fun createBinding(parent: ViewGroup, viewType: Int): PhotoItemBinding {
        val binding = DataBindingUtil.inflate<PhotoItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.photo_item,
                parent,
                false,
                dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.photo?.let {
                callback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: PhotoItemBinding, item: ITEM_PHOTO) {
        binding.photo = item
    }
}