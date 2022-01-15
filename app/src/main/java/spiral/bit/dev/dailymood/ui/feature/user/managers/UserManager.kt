package spiral.bit.dev.dailymood.ui.feature.user.managers

import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import spiral.bit.dev.dailymood.ui.base.callbackConverters.updateUserEmail
import spiral.bit.dev.dailymood.ui.base.callbackConverters.updateUserNameAndPhoto
import spiral.bit.dev.dailymood.ui.base.callbackConverters.updateUserPassword
import spiral.bit.dev.dailymood.ui.feature.user.models.User
import javax.inject.Inject

class UserManager @Inject constructor(authClient: FirebaseAuth) {

    private val firebaseUser = authClient.currentUser

    private var _user = MutableLiveData(
        User(
            firebaseUser?.uid,
            firebaseUser?.email,
            firebaseUser?.phoneNumber,
            firebaseUser?.displayName
        )
    )

    fun getUserLiveData() = _user

    suspend fun updateUserNameAndPhoto(userName: String, photoPath: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(userName)
            .setPhotoUri(photoPath.toUri())
            .build()

        firebaseUser?.updateUserNameAndPhoto(profileUpdates)
    }


    suspend fun updateEmail(email: String) {
        firebaseUser?.updateUserEmail(email)
    }

    suspend fun updatePassword(password: String) {
        firebaseUser?.updateUserPassword(password)
    }
}