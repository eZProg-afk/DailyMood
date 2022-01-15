package spiral.bit.dev.dailymood.ui.feature.registration.emailAuthenticate.view

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.base.callbackConverters.signInWithEmailAwait
import spiral.bit.dev.dailymood.ui.feature.registration.emailAuthenticate.models.mvi.EmailAuthEffect
import spiral.bit.dev.dailymood.ui.feature.registration.emailAuthenticate.models.mvi.EmailAuthState
import spiral.bit.dev.dailymood.ui.feature.registration.registrationTypes.models.ValidationResult
import spiral.bit.dev.dailymood.ui.feature.registration.registrationTypes.models.ValidationType
import javax.inject.Inject

@HiltViewModel
class EmailAuthViewModel @Inject constructor(
    private val authClient: FirebaseAuth
) : BaseViewModel<EmailAuthState, EmailAuthEffect>() {

    override val container = container<EmailAuthState, EmailAuthEffect>(EmailAuthState(validationResult = null))

    fun validateFields(email: String, password: String) = intent {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            reduce {
                state.copy(validationResult = ValidationResult(R.string.invalid_email_error, ValidationType.EMAIL))
            }
        } else if (password.isEmpty()) {
            reduce {
                state.copy(validationResult = ValidationResult(R.string.password_empty_error, ValidationType.PASSWORD))
            }
        } else if (password.length < 7) {
            reduce {
                state.copy(validationResult = ValidationResult(R.string.password_not_enough_large_error, ValidationType.PASSWORD))
            }
        } else {
            authenticateByEmailAndPassword(email, password)
        }
    }

    private fun authenticateByEmailAndPassword(email: String, password: String) = intent {
        postSideEffect(EmailAuthEffect.ShowLoadingDialog)
        runCatching {
            authClient.signInWithEmailAwait(email, password)
        }.onSuccess {
            postSideEffect(EmailAuthEffect.NavigateToMain)
        }.onFailure { throwable ->
            postSideEffect(EmailAuthEffect.ExceptionHappened(throwable))
        }
    }

    fun navigateBack() = intent {
        postSideEffect(EmailAuthEffect.NavigateBack)
    }

    fun navigateToRegistration() = intent {
        postSideEffect(EmailAuthEffect.NavigateToEmailRegistration)
    }
}