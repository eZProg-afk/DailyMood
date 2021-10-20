package spiral.bit.dev.dailymood.ui.feature.registration.email_reg.view

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.egorblagochinnov.validators.Conditions
import com.egorblagochinnov.validators.LiveDataValidator
import com.egorblagochinnov.validators.MuxLiveDataValidator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.ui.feature.registration.models.RegScreenState
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.common.utils.Analytics
import spiral.bit.dev.dailymood.ui.feature.registration.models.mvi.RegEffect
import spiral.bit.dev.dailymood.ui.feature.registration.models.mvi.RegState
import javax.inject.Inject

@HiltViewModel
class EmailRegViewModel @Inject constructor(
    private val authClient: FirebaseAuth
) : BaseViewModel<RegState, RegEffect>() {

    override val container =
        container<RegState, RegEffect>(RegState(RegScreenState.BootStrapState))

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

    fun registrationByEmailAndPassword() {
        authClient.createUserWithEmailAndPassword(
            textEmailSource.value.toString(),
            textPasswordSource.value.toString()
        ).addOnCompleteListener { task ->
                intent {
                    reduce { state.copy(regScreenState = RegScreenState.Loading) }
                    if (task.isSuccessful) {
                        postSideEffect(RegEffect.ToMainScreen)
                        reduce { state.copy(regScreenState = RegScreenState.SuccessAuthorized) }
                        Analytics.send("sign_up")
                    } else {
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

    //TODO ANALYTICS
}