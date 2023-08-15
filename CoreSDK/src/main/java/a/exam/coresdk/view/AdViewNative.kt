package a.exam.coresdk.view

import a.exam.coresdk.R
import a.exam.coresdk.model.AdListener
import a.exam.coresdk.utility.Utility
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.annotation.RequiresApi


// TODO: Reverse Engineering, Refer to
// https://firebase.google.com/docs/reference/android/com/google/android/gms/ads/nativead/NativeAdView
class AdViewNative : FrameLayout {
    private lateinit var mAdListener: AdListener
    private var mNativeADId: String? = ""
    private val logTag = "ExamDemo"

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        handleConstructorAttr(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        handleConstructorAttr(attrs)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        handleConstructorAttr(attrs)
    }

    private fun handleConstructorAttr(attrs: AttributeSet?) {
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

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed) {
            if (Utility.isVisibleForAd(rootView, this, 50)) {
                Utility.startNativeAdTimer(mNativeADId)
            } else {
                Utility.cancelNativeAdTimer(mNativeADId)
            }
        }
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

    fun setUnitId(id: String) {
        mNativeADId = id
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