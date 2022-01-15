package spiral.bit.dev.dailymood.ui.feature.registration.registrationTypes.models.mvi

sealed class RegistrationTypeEffect {
    data class ExceptionHappened(val throwable: Throwable) : RegistrationTypeEffect()
    object NavigateToEmailAuth : RegistrationTypeEffect()
    object NavigateToRegistration : RegistrationTypeEffect()
    object NavigateToMain : RegistrationTypeEffect()
}