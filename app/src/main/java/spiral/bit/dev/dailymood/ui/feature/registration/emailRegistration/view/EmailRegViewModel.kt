package spiral.bit.dev.dailymood.ui.feature.registration.emailRegistration.view

import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.base.callbackConverters.signUpWithEmailAwait
import spiral.bit.dev.dailymood.ui.feature.registration.emailRegistration.models.mvi.EmailRegEffect
import spiral.bit.dev.dailymood.ui.feature.registration.emailRegistration.models.mvi.EmailRegState
import spiral.bit.dev.dailymood.ui.feature.registration.registrationTypes.models.ValidationResult
import spiral.bit.dev.dailymood.ui.feature.registration.registrationTypes.models.ValidationType
import javax.inject.Inject

@HiltViewModel
class EmailRegViewModel @Inject constructor(
    private val authClient: FirebaseAuth
) : BaseViewModel<EmailRegState, EmailRegEffect>() {

    override val container = container<EmailRegState, EmailRegEffect>(EmailRegState(validationResult = null))

    private fun registrationByEmailAndPassword(email: String, password: String) = intent {

        postSideEffect(EmailRegEffect.ShowLoadingDialog)
        runCatching {
            authClient.signUpWithEmailAwait(email, password)
        }.onSuccess {
            postSideEffect(EmailRegEffect.NavigateToMain)
        }.onFailure { throwable ->
            postSideEffect(EmailRegEffect.ExceptionHappened(throwable))
        }
    }

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
            registrationByEmailAndPassword(email, password)
        }
    }

    fun navigateBack() = intent {
        postSideEffect(EmailRegEffect.NavigateBack)
    }

    fun navigateToLogin() = intent {
        postSideEffect(EmailRegEffect.NavigateToEmailAuth)
    }
}