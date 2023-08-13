package a.exam.coresdk.view

import a.exam.coresdk.R
import a.exam.coresdk.model.AdListener
import a.exam.coresdk.utility.Utility
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup

class AdViewNative(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {
    private lateinit var mAdListener: AdListener
    private var mNativeADId: String? = ""
    private val logTag = "ExamDemo"

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed) {
            if (Utility.isVisibleForAd(rootView, this, 50)) {
                Utility.startNativeAdTimer(mNativeADId)
            } else {
                Utility.cancelNativeAdTimer(mNativeADId)
            }
        }
    }

    init {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.AdViewUnit,
            0, 0
        ).apply {

            try {
                mNativeADId = getString(R.styleable.AdViewUnit_unit_id)
            } finally {
                recycle()
            }
        }

        Log.d(logTag, "native unit id: $mNativeADId")
    }

    override fun setOnScrollChangeListener(l: OnScrollChangeListener?) {
        super.setOnScrollChangeListener(l)
    }

    fun show() {
        // TODO: other setting
        visibility = VISIBLE

        if (Utility.isVisibleForAd(rootView, this, 50)) {
            Utility.startNativeAdTimer(mNativeADId)
        }
        mAdListener.onAdImpression()
    }

    fun hide() { // or close
        visibility = GONE
        Utility.startNativeAdTimer(mNativeADId)
        mAdListener.onAdClosed()
    }

    fun setAdListener(listener: AdListener) {
        mAdListener = listener
    }
}