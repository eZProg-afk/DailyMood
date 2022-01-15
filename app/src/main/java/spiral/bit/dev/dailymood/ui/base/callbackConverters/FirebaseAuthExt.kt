package spiral.bit.dev.dailymood.ui.base.callbackConverters

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun FirebaseAuth.signInWithEmailAwait(email: String, password: String) = suspendCoroutine<Unit> { continuation ->
    signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(Unit)
        } else {
            continuation.resumeWithException(task.exception!!)
        }
    }
}

suspend fun FirebaseAuth.signInWithCredentialsAwait(credential: AuthCredential) = suspendCoroutine<Unit> { continuation ->
    signInWithCredential(credential).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(Unit)
        } else {
            continuation.resumeWithException(task.exception!!)
        }
    }
}

suspend fun FirebaseAuth.signUpWithEmailAwait(email: String, password: String) = suspendCoroutine<Unit> { continuation ->
    createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(Unit)
        } else {
            continuation.resumeWithException(task.exception!!)
        }
    }
}