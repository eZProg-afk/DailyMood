package spiral.bit.dev.dailymood.ui.feature.registration

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.egorblagochinnov.validators.Conditions
import com.egorblagochinnov.validators.LiveDataValidator
import com.egorblagochinnov.validators.MuxLiveDataValidator
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
import spiral.bit.dev.dailymood.ui.feature.registration.models.RegScreenState
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.common.utils.Analytics
import spiral.bit.dev.dailymood.ui.feature.registration.common.RegSideEffect
import spiral.bit.dev.dailymood.ui.feature.registration.common.RegState
import javax.inject.Inject

@HiltViewModel
class RegViewModel @Inject constructor(
    private val analytics: FirebaseAnalytics,
    private val authClient: FirebaseAuth
) : BaseViewModel<RegState, RegSideEffect>() {

    override val container =
        container<RegState, RegSideEffect>(RegState(RegScreenState.NOT_DEFINED))

    val textEmailSource = MutableLiveData<String?>()
    val textUsernameSource = MutableLiveData<String?>()
    val textPhoneSource = MutableLiveData<String?>()
    val textPasswordSource = MutableLiveData<String?>()

    val textEmailValidator = LiveDataValidator(textEmailSource).apply {
        addCondition(Conditions.RequiredField())
        addCondition(Conditions.RegEx(Patterns.EMAIL_ADDRESS.toRegex(), "Ваш E-mail невалидный!"))
    }

    val textUsernameValidator = LiveDataValidator(textUsernameSource).apply {
        addCondition(Conditions.RegEx("[a-z]+".toRegex(), "Имя должно быть без спецсимволов!"))
        addCondition(Conditions.TextMinLength(3, "Имя слишком короткое!"))
    }

    val textPhoneValidator = LiveDataValidator(textPhoneSource).apply {
        addCondition(Conditions.RegEx(Patterns.PHONE.toRegex(), "Ваш номер телефона невалидный!"))
        addCondition(Conditions.TextMinLength(11, "Номер телефона слишком короткий!"))
    }

    val textPasswordValidator = LiveDataValidator(textPasswordSource).apply {
        addCondition(Conditions.TextMinLength(8, "Ваш пароль менее 8 символов!"))
        addCondition(Conditions.RequiredField(errorText = "Это поле обязательное!"))
    }

    val muxValidator = MuxLiveDataValidator(
        textEmailValidator,
        textUsernameValidator,
        textPasswordValidator,
        textPhoneValidator
    )

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

    fun authenticateByEmailAndPassword(email: String, password: String) {
        authClient.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                intent {
                    reduce { state.copy(regScreenState = RegScreenState.LOADING) }
                    if (it.isSuccessful) {
                        postSideEffect(RegSideEffect.ToMainScreen)
                        reduce { state.copy(regScreenState = RegScreenState.SUCCESS) }
                        Analytics.send("sign_in")
                    } else {
                        reduce { state.copy(regScreenState = RegScreenState.ERROR) }
                    }
                }
            }
    }

    fun registrationByEmailAndPassword() {
        authClient.createUserWithEmailAndPassword(
            textEmailSource.value.toString(),
            textPasswordSource.value.toString()
        ).addOnCompleteListener {
                intent {
                    reduce { state.copy(regScreenState = RegScreenState.LOADING) }
                    if (it.isSuccessful) {
                        postSideEffect(RegSideEffect.ToMainScreen)
                        reduce { state.copy(regScreenState = RegScreenState.SUCCESS) }
                        Analytics.send("sign_up")
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
        if (authClient.currentUser?.uid != null) {
            postSideEffect(RegSideEffect.ToMainScreen)
        }
    }
}