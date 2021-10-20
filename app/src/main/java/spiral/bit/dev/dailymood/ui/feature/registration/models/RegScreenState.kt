package spiral.bit.dev.dailymood.ui.feature.registration.models

import java.lang.Exception

sealed class RegScreenState {
    object Loading : RegScreenState()
    object SuccessAuthorized : RegScreenState()
    class ErrorAuthorized(val error: Exception) : RegScreenState()
    object BootStrapState : RegScreenState()
}