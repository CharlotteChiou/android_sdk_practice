package a.exam.coresdk.utility

import a.exam.coresdk.model.BaseAdData
import android.graphics.Rect
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object Utility {
    private var mAdTimerMap = HashMap<String, CountDownTimer>()

    // only one banner id
    private var mBannerId: String = ""
    private var mInterstitialAd: String = ""
    private var mKeepList: ArrayList<String> = arrayListOf()

    private const val logTag = "ExamDemo"

    fun onPause() {
        for (timer in mAdTimerMap) {
            mKeepList.add(timer.key)
            timer.value.cancel()
            mAdTimerMap.remove(timer.key)
        }
    }

    fun onResume() {
        for (adId in mKeepList) {
            startTimerWithADId(adId)
        }
    }


    fun onDestroy() {
        for (timer in mAdTimerMap) {
            timer.value.cancel()
        }
        mAdTimerMap.clear()
        mKeepList.clear()
        mBannerId = ""
    }

    fun startBannerTimer(adId: String?) {
        if (!adId.isNullOrEmpty()) {
            mBannerId = adId
            startTimerWithADId(adId)
        }
    }

    fun cancelBannerTimer(adId: String?) {
        if (!adId.isNullOrEmpty()) {
            cancelTimerWithADId(adId)
            mBannerId = ""
        }
    }

    fun startInterstitialAdTimer(adId: String?) {
        if (!adId.isNullOrEmpty()) {
            mInterstitialAd = adId
            startTimerWithADId(adId)
        }
    }

    fun cancelInterstitialAdTimer(adId: String?) {
        if (!adId.isNullOrEmpty()) {
            cancelTimerWithADId(mInterstitialAd)
            mInterstitialAd = ""
        }
    }

    fun checkRecyclerViewAdState(recyclerView: RecyclerView, items: List<Any>) {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstPosition = linearLayoutManager.findFirstVisibleItemPosition()
        val lastPosition = linearLayoutManager.findLastVisibleItemPosition()

        val globalVisibleRect = Rect()
        recyclerView.getGlobalVisibleRect(globalVisibleRect)
        for (pos in firstPosition..lastPosition) {
            val view = linearLayoutManager.findViewByPosition(pos)
            if (view != null) {
                val percentage = getVisibleHeightPercentage(view)
                val baseAdDataItem = items[pos] as BaseAdData

                handleTimerForListAd(pos, percentage.toInt(), baseAdDataItem.isAd)

                (items[pos] as BaseAdData).percent = percentage.toInt()
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun getVisibleHeightPercentage(view: View): Double {
        val itemRect = Rect()
        val isParentViewEmpty = view.getLocalVisibleRect(itemRect)

        // Find the height of the item.
        val visibleHeight = itemRect.height().toDouble()
        val height = view.measuredHeight

        val viewVisibleHeightPercentage = visibleHeight / height * 100

        return if (isParentViewEmpty) {
            viewVisibleHeightPercentage
        } else {
            0.0
        }
    }

    private fun handleTimerForListAd(pos: Int, percent: Int, isAd: Boolean) {
        val mapKey = pos.toString()

        if (percent >= 50 && isAd) {
            startTimerWithADId(mapKey)

        } else {
            cancelTimerWithADId(mapKey)
        }
    }

    private fun startTimerWithADId(adId: String) {
        if (!mAdTimerMap.containsKey(adId)) {
            Log.d(logTag, "ad: $adId, is start.")
            val timer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    // TODO: send data to server
                    Log.d(logTag, "ad : $adId, is finish.")
                }
            }
            timer.start()
            mAdTimerMap[adId] = timer
        }
    }

    private fun cancelTimerWithADId(adId: String) {
        if (mAdTimerMap.containsKey(adId)) {
            val timer = mAdTimerMap[adId]
            timer?.cancel()
            mAdTimerMap.remove(adId)
            Log.d(logTag, "ad: $adId, is cancel.")
        }
    }
}