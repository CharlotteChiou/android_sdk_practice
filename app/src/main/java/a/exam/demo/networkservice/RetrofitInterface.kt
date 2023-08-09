package a.exam.demo.networkservice

import a.exam.demo.model.MovieResponse
import a.exam.demo.model.deprecated.NewsResponse
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface RetrofitInterface {

    @GET(AllApi.SEARCH_NEWS)
    suspend fun getNewsList(@HeaderMap headers: Map<String, String>): NewsResponse

    @GET(AllApi.SEARCH_MOVIE)
    suspend fun getMovieList(@HeaderMap headers: Map<String, String>): MovieResponse

}