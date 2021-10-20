package spiral.bit.dev.dailymood.ui.feature.registration.email_auth.models.mvi

import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class EmailAuthEffect : SideEffectMarker {
    object NavigateToMain : EmailAuthEffect()
}