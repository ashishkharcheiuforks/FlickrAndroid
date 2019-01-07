package com.hucet.flickr.view

import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hucet.flickr.ArgKey
import com.hucet.flickr.R
import com.hucet.flickr.databinding.PhotoItemBinding
import com.hucet.flickr.utils.AppExecutors
import com.hucet.flickr.view.detail.FlickrDetailFragment
import com.hucet.flickr.view.search.FlickrSearchFragment
import com.hucet.flickr.view.search.KeywordAdapter
import com.hucet.flickr.view.search.SearchViewInterface
import com.hucet.flickr.vo.Photo
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_flickr_main.keywordRecyclerView
import kotlinx.android.synthetic.main.activity_flickr_main.toolbar
import javax.inject.Inject

class FlickrSearchActivity : AppCompatActivity(), HasSupportFragmentInjector, SearchViewInterface {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @IdRes
    private val containerIdRes = R.id.content

    private val keywords = listOf("Apple", "Banana", "Amazon", "Cat", "Dog", "Developer", "Style", "Share", "Tyler", "Good")
    private var keyword: String = ""

    @Inject
    lateinit var appExecutors: AppExecutors

    private val keywordAdapter by lazy {
        KeywordAdapter(appExecutors) {
            search(it)
            updateToolbarTitle(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        keyword = savedInstanceState?.getString(ArgKey.Keyword.name) ?: keywords.first()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flickr_main)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(containerIdRes, FlickrSearchFragment.newInstance(keyword))
                    .commit()
        }
        initKeywordRecyclerView()
    }

    private fun search(keyword: String) {
        (supportFragmentManager.findFragmentById(containerIdRes) as? FlickrSearchFragment)?.search(keyword)
    }

    private fun updateToolbarTitle(title: String) {
        supportActionBar?.title = getString(R.string.prefix_search_toolbar_title, title)
    }

    private fun initKeywordRecyclerView() {
        keywordRecyclerView.apply {
            adapter = keywordAdapter
            layoutManager = LinearLayoutManager(this@FlickrSearchActivity, RecyclerView.HORIZONTAL, false)
        }
        keywordAdapter.submitList(keywords)
        updateToolbarTitle(keyword)
    }

    override fun navigateDetail(photoBinding: PhotoItemBinding, photo: Photo) {
        val fromFragment = supportFragmentManager.findFragmentById(containerIdRes) ?: return

        val context = this
        val toFragment = FlickrDetailFragment.newInstance(photo).apply {
            val transition = TransitionInflater.from(context).inflateTransition(R.transition.image_shared_element_transition)
            sharedElementEnterTransition = transition
            sharedElementReturnTransition = transition
            enterTransition = TransitionSet().addTransition(Fade())
        }

        fromFragment.apply {
        }
        supportFragmentManager
                .beginTransaction()
                .addSharedElement(photoBinding.photoImageView, getString(R.string.flickr_transition_name))
                .replace(containerIdRes, toFragment)
                .addToBackStack(null)
                .commit()
        keywordRecyclerView.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val searchFragment = supportFragmentManager.findFragmentById(containerIdRes) as? FlickrSearchFragment
        if (searchFragment != null)
            keywordRecyclerView.visibility = View.VISIBLE
    }
    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(ArgKey.Keyword.name, keyword)
        super.onSaveInstanceState(outState)
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}