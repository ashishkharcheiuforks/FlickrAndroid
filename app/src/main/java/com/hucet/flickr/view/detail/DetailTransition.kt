package com.hucet.flickr.view.detail

import android.content.Context
import android.transition.TransitionInflater
import android.transition.TransitionSet
import com.hucet.flickr.R

class DetailTransition: TransitionSet {
    constructor(context: Context) {
        val transition = TransitionInflater.from(context).inflateTransition(R.transition.image_shared_element_transition)
        addTransition(transition)
    }
}