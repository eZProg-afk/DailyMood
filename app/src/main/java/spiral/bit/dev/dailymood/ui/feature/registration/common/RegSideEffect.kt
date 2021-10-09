package spiral.bit.dev.dailymood.ui.feature.registration.common

import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class RegSideEffect : SideEffectMarker {
    data class Toast(val msg: Int) : RegSideEffect()
    object NavigateToLogin : RegSideEffect()
    object NavigateToReg : RegSideEffect()
    object ToMainScreen : RegSideEffect()
}