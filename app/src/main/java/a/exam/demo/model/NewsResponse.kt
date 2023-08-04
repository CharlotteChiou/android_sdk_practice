package a.exam.demo.model

import com.google.gson.annotations.SerializedName

data class NewsResponse(

    @SerializedName("status") var status: String? = null,
    @SerializedName("total_hits") var totalHits: Int? = null,
    @SerializedName("page") var page: Int? = null,
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("page_size") var pageSize: Int? = null,
    @SerializedName("articles") var articles: ArrayList<Articles> = arrayListOf(),
    @SerializedName("user_input") var userInput: UserInput? = UserInput()

)