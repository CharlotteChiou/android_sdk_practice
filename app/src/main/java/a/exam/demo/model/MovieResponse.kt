package a.exam.demo.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(

    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var results: List<Results> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
)