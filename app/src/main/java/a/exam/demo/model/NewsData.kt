package a.exam.demo.model

data class NewsData(
    var title: String? = "",
    var summary: String? = "",
    var percent: Int = 0,
    var isAd: Boolean = false
)