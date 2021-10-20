package spiral.bit.dev.dailymood.ui.base

import android.util.Log

object Logger {

    fun logDebug(msg: String) = Log.d(DEBUG_TAG, msg)

    fun logError(throwable: Throwable?) = throwable?.localizedMessage?.let {
        Log.e(ERROR_TAG, it)
    } ?: run {
        "Error happens, throwable is null"
    }

    private const val DEBUG_TAG = "DEBUG_TAG"
    private const val ERROR_TAG = "ERROR_TAG"
}