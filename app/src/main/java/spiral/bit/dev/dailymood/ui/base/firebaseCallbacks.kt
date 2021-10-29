package spiral.bit.dev.dailymood.ui.base

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.mlkit.vision.face.Face
import java.lang.IllegalStateException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun FirebaseUser.updateUserEmail(email: String) = suspendCoroutine<String> { continuation ->
    updateEmail(email).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(task.result.toString())
        } else {
            continuation.resumeWithException(FailResumeException("Failed to get value"))
        }
    }
}

suspend fun FirebaseUser.updateUserPassword(password: String) = suspendCoroutine<String> { continuation ->
    updatePassword(password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(password)
        }
    }
}

suspend fun FirebaseUser.updateUserNameAndPhoto(profileUpdates: UserProfileChangeRequest) =
    suspendCoroutine<UserProfileChangeRequest> { continuation ->
        updateProfile(profileUpdates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(profileUpdates)
            }
        }
    }

suspend fun <T> Task<List<T>>.listenAwait() = suspendCoroutine<List<T>> { continuation ->
    addOnCompleteListener {
        if (it.isSuccessful) {
            continuation.resume(it.result)
        } else {
            continuation.resumeWithException(it.exception!!)
        }
    }
}

class FailResumeException(msg: String) : IllegalStateException(msg)