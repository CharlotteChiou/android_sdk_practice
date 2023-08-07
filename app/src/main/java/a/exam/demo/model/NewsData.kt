package a.exam.demo.model

import a.exam.coresdk.BaseAdData

data class NewsData(
    var title: String? = "",
    var summary: String? = "",
    override var percent: Int = 0,
    override var isAd: Boolean = false
) : BaseAdData(percent, isAd)