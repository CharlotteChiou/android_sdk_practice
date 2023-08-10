package a.exam.demo.viewmodel

import a.exam.demo.R
import a.exam.demo.networkservice.ApiState
import a.exam.demo.repository.ItemRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainActivityVM(application: Application) : AndroidViewModel(application) {
    val wMessage: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)

    private val itemRepository = ItemRepository()

    fun getMovieList() = viewModelScope.launch {
        wMessage.value = ApiState.Loading
        val headers = HashMap<String, String>()
        headers["Authorization"] = getApplication<Application>().getString(R.string.movie_api_key)
        itemRepository.getMovieList(headers)
            .catch { e ->
                wMessage.value = ApiState.Failure(e)
            }.collect { data ->
                wMessage.value = ApiState.Success(data)
            }
    }
}
