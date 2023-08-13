package a.exam.coresdk.network

class AdRequest {
    // TODO: design request ad model
    interface Result {
        fun onSuccess()
        fun onFailed()
    }

    fun loadAd(result: Result) {
        // Do something

        // if success
        result.onSuccess()

        // if failed
//        result.onFailed()
    }
}