package spiral.bit.dev.dailymood.ui.feature.registration.emailRegistration.models.mvi

sealed class EmailRegEffect {
    data class ExceptionHappened(val throwable: Throwable) : EmailRegEffect()
    object ShowLoadingDialog : EmailRegEffect()
    object NavigateBack : EmailRegEffect()
    object NavigateToEmailAuth : EmailRegEffect()
    object NavigateToMain : EmailRegEffect()
}