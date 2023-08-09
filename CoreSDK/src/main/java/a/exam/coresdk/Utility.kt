package a.exam.coresdk

import android.graphics.Rect
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object Utility {
    private var mAdTimerMap = HashMap<String, CountDownTimer>()

    private const val logTag = "ExamDemo" // TODO Delete
    fun checkAdState(recyclerView: RecyclerView, items: List<Any>) {
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

                handleTimerForAd(pos, percentage.toInt(), baseAdDataItem.isAd)
            }
        }
    }

    fun getVisibleHeightPercentage(view: View): Double {
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

    private fun handleTimerForAd(pos: Int, percent: Int, isAd: Boolean) {
        val mapKey = pos.toString()

        if (percent >= 50 && isAd) {
            if (!mAdTimerMap.containsKey(mapKey)) {
                Log.d(logTag, "pos: $pos, is start.")
                val timer = object : CountDownTimer(60000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {

                    }

                    override fun onFinish() {
                        // TODO: send data to server
                        Log.d(logTag, "pos: $pos, is finish.")
                    }
                }
                timer.start()
                mAdTimerMap[mapKey] = timer
            }
        } else {
            if (mAdTimerMap.containsKey(mapKey)) {
                val timer = mAdTimerMap[pos.toString()]
                timer?.cancel()
                mAdTimerMap.remove(mapKey)
                Log.d(logTag, "pos: $pos, is cancel.")
            }
        }
    }
}