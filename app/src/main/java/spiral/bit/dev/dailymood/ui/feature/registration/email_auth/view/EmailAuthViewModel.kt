package spiral.bit.dev.dailymood.ui.feature.registration.email_auth.view

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.egorblagochinnov.validators.Conditions
import com.egorblagochinnov.validators.LiveDataValidator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.common.utils.Analytics
import spiral.bit.dev.dailymood.ui.feature.registration.email_auth.models.mvi.EmailAuthEffect
import spiral.bit.dev.dailymood.ui.feature.registration.email_auth.models.mvi.EmailAuthState
import spiral.bit.dev.dailymood.ui.feature.registration.models.RegScreenState
import javax.inject.Inject

@HiltViewModel
class EmailAuthViewModel @Inject constructor(
    private val authClient: FirebaseAuth
) : BaseViewModel<EmailAuthState, EmailAuthEffect>() {

    //TODO USER MANAGER

    override val container =
        container<EmailAuthState, EmailAuthEffect>(EmailAuthState(RegScreenState.BootStrapState))

    val textEmailSource = MutableLiveData<String?>()
    val textPasswordSource = MutableLiveData<String?>()

    val textEmailValidator = LiveDataValidator(textEmailSource).apply {
        addCondition(Conditions.RequiredField())
        addCondition(Conditions.RegEx(Patterns.EMAIL_ADDRESS.toRegex(), "Ваш E-mail невалидный!"))
    }

    val textPasswordValidator = LiveDataValidator(textPasswordSource).apply {
        addCondition(Conditions.TextMinLength(8, "Ваш пароль менее 8 символов!"))
        addCondition(Conditions.RequiredField(errorText = "Это поле обязательное!"))
    }

    fun authenticateByEmailAndPassword(email: String, password: String) {
        authClient.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                intent {
                    reduce { state.copy(regScreenState = RegScreenState.Loading) }
                    if (it.isSuccessful) {
                        postSideEffect(EmailAuthEffect.NavigateToMain)
                        reduce { state.copy(regScreenState = RegScreenState.SuccessAuthorized) }
                        Analytics.send("sign_in")
                    } else {
                        reduce { state.copy(regScreenState = RegScreenState.ErrorAuthorized(it.exception!!)) }
                    }
                }
            }
    }
}