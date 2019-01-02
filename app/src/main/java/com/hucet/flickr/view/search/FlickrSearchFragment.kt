package com.hucet.flickr.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hucet.flickr.R
import com.hucet.flickr.databinding.FragmentFlickrSearchBinding
import com.hucet.flickr.di.Injectable
import com.hucet.flickr.utils.AppExecutors
import com.hucet.flickr.utils.autoCleared
import com.hucet.flickr.view.common.databinding.FragmentDataBindingComponent
import kotlinx.android.synthetic.main.fragment_flickr_search.*
import javax.inject.Inject

class FlickrSearchFragment : Fragment(), Injectable {

    companion object {
        fun newInstance(): FlickrSearchFragment {
            return FlickrSearchFragment()
        }
    }

    private val keywords = listOf("Apple", "Banana", "Amazon", "Cat", "Dog", "Developer", "Style", "Share", "Tyler", "Good")
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var keywordAdapter by autoCleared<KeywordAdapter>()

    val viewModel by lazy {
        viewModelFactory.create(FlickrSearchViewModel::class.java)
    }

    var binding by autoCleared<FragmentFlickrSearchBinding>()

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_flickr_search,
                container,
                false,
                dataBindingComponent
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initKeywordRecyclerView()
    }

    private fun initKeywordRecyclerView() {
        keywordAdapter = KeywordAdapter(appExecutors) {

        }
        keywordRecyclerView.apply {
            adapter = keywordAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        keywordAdapter.submitList(keywords)
    }
}