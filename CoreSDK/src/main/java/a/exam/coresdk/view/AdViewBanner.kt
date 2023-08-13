package a.exam.coresdk.view

import a.exam.coresdk.R
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View

class AdViewBanner(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var mBannerADId: String? = ""
    private val logTag = "ExamDemo"
    private var text: String = ""

    init {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.AdViewUnit,
            0, 0
        ).apply {

            try {
                mBannerADId = getString(R.styleable.AdViewUnit_unit_id)
            } finally {
                recycle()
            }
        }

        Log.d(logTag, "unit id: $mBannerADId")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    fun setSize(width: Int, height: Int) {
        layoutParams.width = width
        layoutParams.height = height
    }

    fun setBannerADId(id: String) {
        mBannerADId = id
    }

    fun getBannerADId(): String? {
        return mBannerADId
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}