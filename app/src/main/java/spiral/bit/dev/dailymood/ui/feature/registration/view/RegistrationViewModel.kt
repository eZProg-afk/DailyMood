package spiral.bit.dev.dailymood.ui.feature.registration.view

import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.feature.registration.models.RegScreenState
import spiral.bit.dev.dailymood.ui.feature.registration.models.mvi.RegEffect
import spiral.bit.dev.dailymood.ui.feature.registration.models.mvi.RegState
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authClient: FirebaseAuth
) : BaseViewModel<RegState, RegEffect>() {

    override val container = container<RegState, RegEffect>(RegState(RegScreenState.BootStrapState))

    fun handleFBToken(accessToken: AccessToken) {
        val credentials = FacebookAuthProvider.getCredential(accessToken.token)
        authClient.signInWithCredential(credentials).addOnCompleteListener { task ->
            intent {
                reduce { state.copy(regScreenState = RegScreenState.Loading) }
                if (task.isSuccessful) {
                    postSideEffect(RegEffect.ToMainScreen)
                    reduce { state.copy(regScreenState = RegScreenState.SuccessAuthorized) }
                } else {
                    postSideEffect(RegEffect.Toast(R.string.error_fb_reg_try_later))
                    reduce { state.copy(regScreenState = RegScreenState.ErrorAuthorized(task.exception!!)) }
                }
            }
        }
    }

    fun toLogin() = intent {
        postSideEffect(RegEffect.NavigateToLogin)
    }

    fun toRegistration() = intent {
        postSideEffect(RegEffect.NavigateToReg)
    }

    fun errorRegisterCallback() = intent {
        postSideEffect(RegEffect.Toast(R.string.error_fb_reg_try_later))
    }

    fun checkIsAlreadyAuthorized() = intent {
        if (authClient.currentUser?.uid != null) {
            postSideEffect(RegEffect.ToMainScreen)
            //TODO USER MANAGER
        }
    }
}