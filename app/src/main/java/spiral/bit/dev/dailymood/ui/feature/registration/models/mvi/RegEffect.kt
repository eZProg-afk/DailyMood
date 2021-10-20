package spiral.bit.dev.dailymood.ui.feature.registration.models.mvi

import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class RegEffect : SideEffectMarker {
    data class Toast(val msg: Int) : RegEffect()
    object NavigateToLogin : RegEffect()
    object NavigateToReg : RegEffect()
    object ToMainScreen : RegEffect()
}