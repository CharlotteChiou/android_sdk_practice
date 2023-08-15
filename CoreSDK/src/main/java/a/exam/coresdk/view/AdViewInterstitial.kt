package a.exam.coresdk.view

import a.exam.coresdk.R
import a.exam.coresdk.model.AdInterstitialListener
import a.exam.coresdk.network.AdRequest
import a.exam.coresdk.utility.Utility
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup

// TODO: Reverse Engineering, modify to Object
// https://developers.google.com/android/reference/com/google/android/gms/ads/interstitial/InterstitialAd
class AdViewInterstitial(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {
    private var mInterstitialADId: String? = ""
    private val logTag = "ExamDemo"

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    init {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.AdViewUnit,
            0, 0
        ).apply {

            try {
                mInterstitialADId = getString(R.styleable.AdViewUnit_unit_id)
            } finally {
                recycle()
            }
        }

        Log.d(logTag, "interstitial unit id: $mInterstitialADId")
    }

    fun setUnitId(id: String) {
        mInterstitialADId = id
    }

    fun load(context: Context, adRequest: AdRequest, listener: AdInterstitialListener) {
        loadAdFromServer(context, adRequest, listener)
    }

    fun load(
        context: Context,
        adId: String,
        adRequest: AdRequest,
        listener: AdInterstitialListener
    ) {
        mInterstitialADId = adId
        loadAdFromServer(context, adRequest, listener)
    }

    fun show(context: Context) {
        // TODO: other setting
        bringToFront()
        if (Utility.isVisibleForAd(rootView, this, 50)) {
            Utility.startInterstitialAdTimer(mInterstitialADId)
        }
    }

    fun hide() {
        Utility.cancelInterstitialAdTimer(mInterstitialADId)
    }

    private fun loadAdFromServer(
        context: Context,
        adRequest: AdRequest,
        listener: AdInterstitialListener
    ) {
        // TODO: connect to server
        adRequest.loadAd(object : AdRequest.Result {
            override fun onSuccess() {
                listener.onAdLoaded()
            }

            override fun onFailed() {
                listener.onAdFailedToLoad()
            }
        })
    }
}