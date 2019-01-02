package com.hucet.flickr.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hucet.flickr.R
import com.hucet.flickr.api.ApiErrorResponse
import com.hucet.flickr.api.ApiSuccessResponse
import com.hucet.flickr.databinding.FragmentFlickrSearchBinding
import com.hucet.flickr.di.Injectable
import com.hucet.flickr.utils.AppExecutors
import com.hucet.flickr.utils.autoCleared
import com.hucet.flickr.view.common.databinding.FragmentDataBindingComponent
import kotlinx.android.synthetic.main.fragment_flickr_search.*
import timber.log.Timber
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

    var photoAdapter by autoCleared<PhotoAdapter>()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.results.observe(this, Observer {
            when (it) {
                is ApiSuccessResponse -> {
                    photoAdapter.submitList(it.body.photos.photo)
                }
                is ApiErrorResponse -> {
                    Timber.e(it.errorMessage)
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initKeywordRecyclerView()
        initPhotoAdapter()
    }

    private fun initKeywordRecyclerView() {
        keywordAdapter = KeywordAdapter(appExecutors) {
            viewModel.search(it)
        }
        keywordRecyclerView.apply {
            adapter = keywordAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        }
        keywordAdapter.submitList(keywords)
    }

    private fun initPhotoAdapter() {
        photoAdapter = PhotoAdapter(dataBindingComponent, appExecutors) {
        }
        photoRecyclerView.apply {
            adapter = photoAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
        viewModel.search(keywords.first())
    }
}