package a.exam.demo.networkservice

import a.exam.demo.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface RetrofitInterface {

    @GET(AllApi.SEARCH_MOVIE)
    suspend fun getMovieList(@HeaderMap headers: Map<String, String>): MovieResponse

}