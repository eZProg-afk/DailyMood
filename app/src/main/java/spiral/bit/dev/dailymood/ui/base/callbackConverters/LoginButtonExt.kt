package spiral.bit.dev.dailymood.ui.base.extensions

import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun LoginButton.login(callbackManager: CallbackManager) = suspendCoroutine<LoginResult> { continuation ->
    registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
        override fun onSuccess(result: LoginResult?) {
            continuation.resume(result!!)
        }

        override fun onCancel() {
        }

        override fun onError(error: FacebookException) {
            continuation.resumeWithException(error)
        }
    })
}