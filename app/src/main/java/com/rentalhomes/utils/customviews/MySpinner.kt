package com.rentalhomes.utils.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner
import android.widget.Spinner

class MySpinner : AppCompatSpinner {
    private var mToggleFlag = true

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int, mode: Int) : super(
        context!!, attrs, defStyle, mode
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, mode: Int) : super(context!!, mode) {}
    constructor(context: Context?) : super(context!!) {}

    override fun getSelectedItemPosition(): Int {
        return if (!mToggleFlag) {
            0 // get us to the first element
        } else super.getSelectedItemPosition()
    }

    override fun performClick(): Boolean {
        mOpenInitiated = true
        if (mListener != null) {
            mListener!!.onSpinnerOpened(this)
        }
        mToggleFlag = false
        val result = super.performClick()
        mToggleFlag = true
        return result
    }

    interface OnSpinnerEventsListener {
        fun onSpinnerOpened(spinner: Spinner?)
        fun onSpinnerClosed(spinner: Spinner?)
    }

    private var mListener: OnSpinnerEventsListener? = null
    private var mOpenInitiated = false
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasBeenOpened() && hasFocus) {
            performClosedEvent()
        }
    }

    fun setSpinnerEventsListener(onSpinnerEventsListener: OnSpinnerEventsListener?) {
        mListener = onSpinnerEventsListener
    }

    private fun performClosedEvent() {
        mOpenInitiated = false
        if (mListener != null) {
            mListener!!.onSpinnerClosed(this)
        }
    }

    private fun hasBeenOpened(): Boolean {
        return mOpenInitiated
    }

}