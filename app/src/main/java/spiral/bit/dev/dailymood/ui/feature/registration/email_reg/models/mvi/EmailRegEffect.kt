package spiral.bit.dev.dailymood.ui.feature.registration.email_reg.models.mvi

import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class EmailRegEffect : SideEffectMarker {
    object NavigateToEmailAuth : EmailRegEffect()
    object NavigateToEmailReg : EmailRegEffect()
}