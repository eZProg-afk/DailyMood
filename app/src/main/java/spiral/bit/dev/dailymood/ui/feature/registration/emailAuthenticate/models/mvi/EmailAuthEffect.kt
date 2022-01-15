package spiral.bit.dev.dailymood.ui.feature.registration.emailAuthenticate.models.mvi

sealed class EmailAuthEffect {
    object NavigateToMain : EmailAuthEffect()
    data class ExceptionHappened(val throwable: Throwable) : EmailAuthEffect()
    object ShowLoadingDialog : EmailAuthEffect()
    object NavigateBack : EmailAuthEffect()
    object NavigateToEmailRegistration : EmailAuthEffect()
}