package com.hucet.flickr.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.hucet.flickr.R
import com.hucet.flickr.databinding.KeywordItemBinding
import com.hucet.flickr.utils.AppExecutors
import com.hucet.flickr.view.common.adapter.DataBoundListAdapter


typealias ITEM = String

class KeywordAdapter constructor(
        appExecutors: AppExecutors,
        private val callback: ((ITEM) -> Unit)?
) : DataBoundListAdapter<ITEM, KeywordItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<ITEM>() {
            override fun areItemsTheSame(oldItem: ITEM, newItem: ITEM): Boolean {
                return oldItem == newItem && oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ITEM, newItem: ITEM): Boolean {
//                TODO
                return oldItem == newItem && oldItem == newItem
            }
        }) {
    override fun createBinding(parent: ViewGroup, viewType: Int): KeywordItemBinding {
        val binding = DataBindingUtil.inflate<KeywordItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.keyword_item,
                parent,
                false
        )
        binding.root.setOnClickListener {
            binding.keyword?.let {
                callback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: KeywordItemBinding, item: ITEM) {
        binding.keyword = item
    }
}