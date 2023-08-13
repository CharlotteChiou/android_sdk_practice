package a.exam.coresdk.model

interface AdListener {
    fun onAdClicked()
    fun onAdClosed()
    fun onAdFailedToLoad()
    fun onAdImpression()
    fun onAdLoaded()
    fun onAdOpened()
}