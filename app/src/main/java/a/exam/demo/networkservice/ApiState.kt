package a.exam.demo.networkservice

sealed class ApiState {
    object Loading : ApiState()
    class Failure(val e: Throwable) : ApiState()
    class Success(val data: Any) : ApiState()
    object Empty : ApiState()
}