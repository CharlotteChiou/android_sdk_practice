package a.exam.demo.viewmodel

import a.exam.demo.networkservice.ApiState
import a.exam.demo.repository.ItemRepository
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainActivityVM(application: Application) : AndroidViewModel(application) {
    val wMessage: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)

    private val itemRepository = ItemRepository()
    private val apiKey = "A1I39GfNvKiN9q1IZVYQWke6l2OtYF664xjvg6smXAA"

    fun getNewsList() = viewModelScope.launch {
        wMessage.value = ApiState.Loading
        val headers = HashMap<String, String>()
        headers["x-api-key"] = apiKey
        itemRepository.getNewsList(headers)
            .catch { e ->
                Log.d("MyTest", "ApiState.Failure")
                wMessage.value = ApiState.Failure(e)
            }.collect { data ->
                Log.d("MyTest", "ApiState.Success")
                wMessage.value = ApiState.Success(data)
            }
    }
}
