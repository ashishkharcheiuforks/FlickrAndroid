package com.hucet.flickr.testing.dialog

import android.os.Bundle

abstract class Instruction {

    var dataContainer = Bundle()
        private set

    abstract val description: String

    fun setData(dataContainer: Bundle) {
        this.dataContainer = dataContainer
    }

    abstract fun checkCondition(): Boolean
}