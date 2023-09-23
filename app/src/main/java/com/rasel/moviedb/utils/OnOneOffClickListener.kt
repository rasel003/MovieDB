package com.paperflymerchantapp.utils

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View

abstract class OnOneOffClickListener : View.OnClickListener {
    private var mLastClickTime: Long = 0

    abstract fun onSingleClick(v: View?)

    override fun onClick(v: View) {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime
        mLastClickTime = currentClickTime
        if (elapsedTime <= MIN_CLICK_INTERVAL) return
        if (!isViewClicked) {
            isViewClicked = true
            startTimer()
        } else {
            return
        }
        onSingleClick(v)
    }

    /**
     * This method delays simultaneous touch events of multiple views.
     */
    private fun startTimer() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ isViewClicked = false }, 3000)
    }

    companion object {
        private const val MIN_CLICK_INTERVAL: Long = 500
        var isViewClicked = false
    }
}