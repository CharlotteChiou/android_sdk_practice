package a.exam.coresdk.init

import android.content.Context

object ExamCoreSDK {
    enum class InitState {
        SUCCESS, FAILED, OTHER
    }

    interface OnInitializationCompleteListener {
        fun onInitializationComplete(state: InitState)
    }

    fun initialize(context: Context, listener: OnInitializationCompleteListener) {
        // TODO: init and setup, eg. network ... other third party

        // if success
        listener.onInitializationComplete(InitState.SUCCESS)

        // if failed
//        listener.onInitializationComplete(InitState.FAILED)

        // if other
//        listener.onInitializationComplete(InitState.OTHER)
    }
}