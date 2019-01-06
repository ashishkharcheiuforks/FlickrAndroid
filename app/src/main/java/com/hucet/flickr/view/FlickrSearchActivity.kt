package com.hucet.flickr.view

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeClipBounds
import android.transition.Fade
import android.transition.TransitionInflater
import android.transition.TransitionSet
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hucet.flickr.R
import com.hucet.flickr.databinding.PhotoItemBinding
import com.hucet.flickr.view.detail.FlickrDetailFragment
import com.hucet.flickr.view.search.FlickrSearchFragment
import com.hucet.flickr.view.search.SearchViewInterface
import com.hucet.flickr.vo.Photo
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_flickr_main.toolbar
import kotlinx.android.synthetic.main.fragment_flickr_search.keywordRecyclerView
import javax.inject.Inject

class FlickrSearchActivity : AppCompatActivity(), HasSupportFragmentInjector, SearchViewInterface {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @IdRes
    private val containerIdRes = R.id.content

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flickr_main)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(containerIdRes, FlickrSearchFragment.newInstance())
                    .commit()
        }
    }

    override fun updateToolbarTitle(title: String) {
        supportActionBar?.title = getString(R.string.prefix_search_toolbar_title, title)
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
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}