package a.exam.demo.repository

import a.exam.demo.model.NewsResponse
import a.exam.demo.networkservice.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.http.HeaderMap

class ItemRepository {
    fun getNewsList(@HeaderMap headers: Map<String, String>): Flow<NewsResponse> = flow {
        val p = RetrofitClient.retrofit.getNewsList(headers)
        emit(p)
    }.flowOn(Dispatchers.IO)
}