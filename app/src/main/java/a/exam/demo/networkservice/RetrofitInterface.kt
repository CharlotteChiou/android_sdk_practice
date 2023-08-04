package a.exam.demo.networkservice

import a.exam.demo.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface RetrofitInterface {

    @GET(AllApi.SEARCH_NEWS)
    suspend fun getNewsList(@HeaderMap headers: Map<String, String>): NewsResponse

}