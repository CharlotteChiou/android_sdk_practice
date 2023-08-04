package a.exam.demo.model

import com.google.gson.annotations.SerializedName

data class UserInput(

    @SerializedName("q") var q: String? = null,
    @SerializedName("search_in") var searchIn: ArrayList<String> = arrayListOf(),
    @SerializedName("lang") var lang: String? = null,
    @SerializedName("not_lang") var notLang: String? = null,
    @SerializedName("countries") var countries: String? = null,
    @SerializedName("not_countries") var notCountries: String? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("to") var to: String? = null,
    @SerializedName("ranked_only") var rankedOnly: String? = null,
    @SerializedName("from_rank") var fromRank: String? = null,
    @SerializedName("to_rank") var toRank: String? = null,
    @SerializedName("sort_by") var sortBy: String? = null,
    @SerializedName("page") var page: Int? = null,
    @SerializedName("size") var size: Int? = null,
    @SerializedName("sources") var sources: String? = null,
    @SerializedName("not_sources") var notSources: ArrayList<String> = arrayListOf(),
    @SerializedName("topic") var topic: String? = null,
    @SerializedName("published_date_precision") var publishedDatePrecision: String? = null

)