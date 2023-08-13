package a.exam.coresdk.view

import a.exam.coresdk.model.AdListener
import a.exam.coresdk.model.AdSize
import a.exam.coresdk.network.AdRequest
import a.exam.coresdk.utility.Utility
import android.content.Context

class AdView(context: Context, view: AdViewBanner) {
    private var mContext: Context
    private lateinit var mBannerAd: AdViewBanner
    private lateinit var mAdListener: AdListener

    init {
        mContext = context
        mBannerAd = view
    }

    fun onPause() {
        Utility.onPause()
    }

    fun onResume() {
        Utility.onResume()
    }

    fun onDestroy() {
        Utility.onDestroy()
    }

    fun setAdSize(size: AdSize) {
        mBannerAd.setSize(AdSize.getWidth(size), AdSize.getHeight(size))
    }

    fun setAdSize(width: Int, height: Int) {
        mBannerAd.setSize(width, height)
    }

    fun loadAd(request: AdRequest, listener: AdListener) {
        // TODO: load ad and show
        mAdListener = listener

        request.loadAd(object : AdRequest.Result {
            override fun onSuccess() {
                handleLoadSuccess()
            }

            override fun onFailed() {
                mBannerAd.hide()
                mAdListener.onAdFailedToLoad()
            }
        })
    }

    private fun handleLoadSuccess() {
        mBannerAd.setOnClickListener {
            // TODO: onclick
            mAdListener.onAdClicked()
        }
        mBannerAd.show()

        mAdListener.onAdLoaded()
    }
}