package com.hucet.flickr.view

import android.content.Context
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionInflater
import android.transition.TransitionSet
import android.view.Gravity
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import com.hucet.flickr.R
import com.hucet.flickr.view.detail.DetailTransition
import com.hucet.flickr.view.detail.FlickrDetailFragment
import com.hucet.flickr.view.search.FlickrSearchFragment
import com.hucet.flickr.view.search.SearchNavigation
import com.hucet.flickr.view.search.SearchTransition
import com.hucet.flickr.vo.Photo
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class FlickrSearchActivity : AppCompatActivity(), HasSupportFragmentInjector, SearchNavigation {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(android.R.id.content, FlickrSearchFragment.newInstance())
                    .commit()
        }
    }

    override fun navigateDetail(fromFragment: Fragment, imageView: ImageView, photo: Photo) {
        imageView.transitionName = getString(R.string.flickr_transition_name)
        val toFragment = FlickrDetailFragment.newInstance(photo).apply {
            val detailTranstion = DetailTransition(this@FlickrSearchActivity)
            sharedElementEnterTransition = detailTranstion
        }

        fromFragment.apply {
            exitTransition = SearchTransition(this@FlickrSearchActivity)
        }

        supportFragmentManager
                .beginTransaction()
                .setReorderingAllowed(true) // Optimize for shared element transition
                .addSharedElement(imageView, getString(R.string.flickr_transition_name))
                .replace(android.R.id.content, toFragment)
                .addToBackStack(null)
                .commit()
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}