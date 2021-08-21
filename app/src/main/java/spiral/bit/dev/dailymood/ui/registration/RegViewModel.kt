package spiral.bit.dev.dailymood.ui.registration

import com.facebook.AccessToken
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.ui.registration.models.RegScreenState
import spiral.bit.dev.dailymood.helpers.infixSendEvent
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.registration.common.RegSideEffect
import spiral.bit.dev.dailymood.ui.registration.common.RegState
import javax.inject.Inject

@HiltViewModel
class RegViewModel @Inject constructor(
    private val analytics: FirebaseAnalytics,
    private val authClient: FirebaseAuth
) : BaseViewModel<RegState, RegSideEffect>() {

    override val container =
        container<RegState, RegSideEffect>(RegState(RegScreenState.NOT_DEFINED))

    fun handleFBToken(accessToken: AccessToken) {
        val credentials = FacebookAuthProvider.getCredential(accessToken.token)
        authClient.signInWithCredential(credentials).addOnCompleteListener { task ->
            intent {
                reduce { state.copy(regScreenState = RegScreenState.LOADING) }
                if (task.isSuccessful) {
                    postSideEffect(RegSideEffect.ToMainScreen)
                    reduce { state.copy(regScreenState = RegScreenState.SUCCESS) }
                } else {
                    postSideEffect(RegSideEffect.Toast(R.string.error_fb_reg_try_later))
                    reduce { state.copy(regScreenState = RegScreenState.ERROR) }
                }
            }
        }
    }

    fun loginByEmailAndPassword(email: String, password: String) {
        authClient.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                intent {
                    reduce { state.copy(regScreenState = RegScreenState.LOADING) }
                    if (it.isSuccessful) {
                        postSideEffect(RegSideEffect.ToMainScreen)
                        reduce { state.copy(regScreenState = RegScreenState.SUCCESS) }
                    } else {
                        reduce { state.copy(regScreenState = RegScreenState.ERROR) }
                    }
                }
            }
    }

    fun regByEmailAndPassword(email: String, password: String) {
        authClient.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                intent {
                    reduce { state.copy(regScreenState = RegScreenState.LOADING) }
                    if (it.isSuccessful) {
                        postSideEffect(RegSideEffect.ToMainScreen)
                        reduce { state.copy(regScreenState = RegScreenState.SUCCESS) }
                        analytics infixSendEvent "sign_up"
                    } else {
                        reduce { state.copy(regScreenState = RegScreenState.ERROR) }
                    }
                }
            }
    }

    fun toLogin() = intent {
        postSideEffect(RegSideEffect.NavigateToLogin)
    }

    fun toRegistration() = intent {
        postSideEffect(RegSideEffect.NavigateToReg)
    }

    fun errorRegisterCallback() = intent {
        postSideEffect(RegSideEffect.Toast(R.string.error_fb_reg_try_later))
    }

    fun checkIsAlreadyAuthorized() = intent {
        if (authClient.currentUser?.uid != null)
            postSideEffect(RegSideEffect.ToMainScreen)
    }

}