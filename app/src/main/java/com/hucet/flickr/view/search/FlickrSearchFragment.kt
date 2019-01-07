package com.hucet.flickr.view.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hucet.flickr.ArgKey
import com.hucet.flickr.R
import com.hucet.flickr.databinding.FragmentFlickrSearchBinding
import com.hucet.flickr.databinding.PhotoItemBinding
import com.hucet.flickr.di.Injectable
import com.hucet.flickr.glide.GlideApp
import com.hucet.flickr.utils.AppExecutors
import com.hucet.flickr.utils.autoCleared
import com.hucet.flickr.view.common.databinding.FragmentDataBindingComponent
import com.hucet.flickr.vo.Photo
import com.hucet.flickr.vo.Status
import kotlinx.android.synthetic.main.fragment_flickr_search.photoRecyclerView
import kotlinx.android.synthetic.main.fragment_flickr_search.swipeRefresh
import timber.log.Timber
import javax.inject.Inject

interface SearchViewInterface {
    fun navigateDetail(photoBinding: PhotoItemBinding, photo: Photo)
}

class FlickrSearchFragment : Fragment(), Injectable {
    companion object {
        fun newInstance(keyword: String): FlickrSearchFragment {
            return FlickrSearchFragment().apply {
                arguments = bundleOf(ArgKey.Keyword.name to keyword)
            }
        }
    }

    private val keyword by lazy {
        arguments?.getString(ArgKey.Keyword.name)
    }
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    var photoAdapter by autoCleared<PhotoAdapter>()

    val viewModel by lazy {
        viewModelFactory.create(FlickrSearchViewModel::class.java)
    }

    var binding by autoCleared<FragmentFlickrSearchBinding>()

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    private var searchNavigator: SearchViewInterface? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SearchViewInterface)
            searchNavigator = context
        else
            throw TypeCastException("$context + must implement EnrollNavigation")
    }

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

            Timber.i("status: [${it.status}]\ndata: [${it.data?.size}]\nerror: [${it.message}]")
            when {
                it.status == Status.LOADING -> {
                    showProgressBar()
                }
                it.data != null -> {
                    hideProgressBar()
                    photoAdapter.submitList(it.data)
                }
                it.message != null -> {
                    hideProgressBar()
                    AlertDialog.Builder(requireContext())
                            .setTitle("오류")
                            .setMessage(it.message)
                            .setPositiveButton(android.R.string.ok, null)
                            .show()
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPhotoAdapter()
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun initPhotoAdapter() {
        photoAdapter = PhotoAdapter(dataBindingComponent, appExecutors) { itemView, photo ->
            navigateDetail(itemView, photo)
        }
        photoRecyclerView.apply {
            adapter = photoAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
        photoRecyclerView.setRecyclerListener {
            val imageView = (it as? PhotoItemBinding)?.photoImageView
            imageView?.let {
                GlideApp.with(this).clear(it)
            }
        }
        photoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == photoAdapter.itemCount - 1) {
                    viewModel.loadNextPage()
                }
            }
        })
        search(keyword)
    }

    fun search(keyword: String?) {
        viewModel.search(keyword ?: "")
    }

    private fun hideProgressBar() {
        swipeRefresh.isRefreshing = false
    }

    private fun showProgressBar() {
    }

    private fun navigateDetail(photoBinding: PhotoItemBinding, photo: Photo) {
        searchNavigator?.navigateDetail(photoBinding, photo)
    }
}