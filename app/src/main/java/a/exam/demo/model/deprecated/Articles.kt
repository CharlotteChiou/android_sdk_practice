package a.exam.demo.model.deprecated

import com.google.gson.annotations.SerializedName

data class Articles(
    // news api "Monthly API calls limit reached: 50"
    @SerializedName("title") var title: String? = null,
    @SerializedName("author") var author: String? = null,
    @SerializedName("published_date") var publishedDate: String? = null,
    @SerializedName("published_date_precision") var publishedDatePrecision: String? = null,
    @SerializedName("link") var link: String? = null,
    @SerializedName("clean_url") var cleanUrl: String? = null,
    @SerializedName("excerpt") var excerpt: String? = null,
    @SerializedName("summary") var summary: String? = null,
    @SerializedName("rights") var rights: String? = null,
    @SerializedName("rank") var rank: Int? = null,
    @SerializedName("topic") var topic: String? = null,
    @SerializedName("country") var country: String? = null,
    @SerializedName("language") var language: String? = null,
    @SerializedName("authors") var authors: String? = null,
    @SerializedName("media") var media: String? = null,
    @SerializedName("is_opinion") var isOpinion: Boolean? = null,
    @SerializedName("twitter_account") var twitterAccount: String? = null,
    @SerializedName("_score") var Score: Double? = null,
    @SerializedName("_id") var Id: String? = null

)
