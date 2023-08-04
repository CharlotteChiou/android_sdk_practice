package a.exam.coresdk

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object Utility {
    fun getViewVisibilityPercentsScreen(currentView: View): Int {
        var viewPercents = 100
        var rect: Rect = Rect()
        val isVisible = currentView.getLocalVisibleRect(rect)
        if (isVisible) {
            val height = currentView.height
            if (viewIsPartiallyHiddenTop(rect)) {
                viewPercents = (height - rect.top)
            } else if (viewIsPartiallyHiddenBottom(rect, height)) {
                viewPercents = (rect.bottom * 100) / height
            }
        } else {
            viewPercents = 0
        }

        return viewPercents
    }

    private fun viewIsPartiallyHiddenBottom(rect: Rect, height: Int): Boolean {
        return rect.bottom in 1 until height
    }

    private fun viewIsPartiallyHiddenTop(rect: Rect): Boolean {
        return rect.top > 0
    }

    fun getViewSelfVisibilityPercents(linearLayoutManager: LinearLayoutManager): Int {
        var percent = 100
        val position: Int = linearLayoutManager.findFirstVisibleItemPosition()
        val rect = Rect()
        linearLayoutManager.findViewByPosition(position)?.getGlobalVisibleRect(rect)

        return percent
    }

    fun test(recycler: RecyclerView) {
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recycler.layoutManager as LinearLayoutManager
                val firstPosition = layoutManager.findFirstVisibleItemPosition()
                val lastPosition = layoutManager.findLastVisibleItemPosition()

                val globalVisibleRect = Rect()
                val itemVisibleRect = Rect()

                recycler.getGlobalVisibleRect(globalVisibleRect)

                for (pos in firstPosition..lastPosition) {
                    val view = layoutManager.findViewByPosition(pos)
                    if (view != null && view.height > 0 && view.getGlobalVisibleRect(itemVisibleRect)) {
                        val visibilityExtent =
                            if (itemVisibleRect.bottom >= globalVisibleRect.bottom) {
                                val visibleHeight = globalVisibleRect.bottom - itemVisibleRect.top
                                Math.min(visibleHeight.toFloat() / view.height, 1f)
                            } else {
                                val visibleHeight = itemVisibleRect.bottom - globalVisibleRect.top
                                Math.min(visibleHeight.toFloat() / view.height, 1f)
                            }

                        val viewHolder =
                            recycler.findViewHolderForAdapterPosition(pos) as RecyclerView.ViewHolder
//                        viewHolder.setVisibilityExtent(visibilityExtent)

                        // if percentage is needed...
                        val percentage = visibilityExtent * 100

                    }
                }
            }
        })
    }
}