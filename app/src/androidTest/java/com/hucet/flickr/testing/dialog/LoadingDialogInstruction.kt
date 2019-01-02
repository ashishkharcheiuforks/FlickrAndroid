package com.hucet.flickr.testing.dialog

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment

class LoadingDialogInstruction(private val activity: AppCompatActivity, private val tag: String) : Instruction() {
    override val description: String
        get() = "Loading dialog shouldn't be in view hierarchy"

    override fun checkCondition(): Boolean {

        val f = activity.supportFragmentManager.findFragmentByTag(tag) as? DialogFragment
        return f == null
    }
}