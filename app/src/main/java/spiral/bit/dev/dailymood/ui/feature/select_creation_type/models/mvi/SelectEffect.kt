package spiral.bit.dev.dailymood.ui.feature.select_creation_type.models.mvi

import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class SelectEffect : SideEffectMarker {
    object NavigateToCreateManually : SelectEffect()
    object NavigateToCreateVoice : SelectEffect()
    object NavigateToCreateSurvey : SelectEffect()
    object NavigateToCreatePhoto : SelectEffect()
}