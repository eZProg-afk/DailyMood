package spiral.bit.dev.dailymood.ui.feature.registration.registrationTypes.view

import android.content.Intent
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.base.extensions.login
import spiral.bit.dev.dailymood.ui.base.callbackConverters.signInWithCredentialsAwait
import spiral.bit.dev.dailymood.ui.feature.registration.registrationTypes.models.mvi.RegistrationTypeEffect
import spiral.bit.dev.dailymood.ui.feature.registration.registrationTypes.models.mvi.RegistrationTypeState
import javax.inject.Inject

@HiltViewModel
class RegistrationTypeViewModel @Inject constructor(
    private val authClient: FirebaseAuth,
    private val callbackManager: CallbackManager
) : BaseViewModel<RegistrationTypeState, RegistrationTypeEffect>() {

    override val container = container<RegistrationTypeState, RegistrationTypeEffect>(RegistrationTypeState(user = null)) {
        checkIsAlreadyAuthorized()
    }

    private fun checkIsAlreadyAuthorized() = intent {
        if (authClient.currentUser?.uid != null) {
            postSideEffect(RegistrationTypeEffect.NavigateToMain)
        }
    }

    private fun handleFBToken(accessToken: AccessToken) = intent {
        val credentials = FacebookAuthProvider.getCredential(accessToken.token)
        runCatching {
            authClient.signInWithCredential(credentials)
        }.onSuccess {
            postSideEffect(RegistrationTypeEffect.NavigateToMain)
        }.onFailure { throwable ->
            postSideEffect(RegistrationTypeEffect.ExceptionHappened(throwable))
        }
    }

    fun handleGoogleSignInIntent(data: Intent?) = intent {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        runCatching {
            task.getResult(ApiException::class.java)
        }.onSuccess { account ->
            registerWithGoogle(account.idToken!!)
        }.onFailure { throwable ->
            postSideEffect(RegistrationTypeEffect.ExceptionHappened(throwable))
        }
    }

    fun registerFacebookCallback(loginButton: LoginButton) = intent {
        runCatching {
            loginButton.login(callbackManager)
        }.onSuccess { loginResult ->
            handleFBToken(loginResult.accessToken)
        }.onFailure { throwable ->
            postSideEffect(RegistrationTypeEffect.ExceptionHappened(throwable))
        }
    }

    private fun registerWithGoogle(idToken: String) = intent {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        runCatching {
            authClient.signInWithCredentialsAwait(credential)
        }.onSuccess {
            val user = authClient.currentUser
            reduce { state.copy(user = user) }
            postSideEffect(RegistrationTypeEffect.NavigateToMain)
        }.onFailure { throwable ->
            postSideEffect(RegistrationTypeEffect.ExceptionHappened(throwable))
        }
    }

    fun navigateToEmailAuthenticate() = intent {
        postSideEffect(RegistrationTypeEffect.NavigateToEmailAuth)
    }

    fun navigateToEmailRegistration() = intent {
        postSideEffect(RegistrationTypeEffect.NavigateToRegistration)
    }

    fun navigateToMain() = intent {
        postSideEffect(RegistrationTypeEffect.NavigateToMain)
    }
}