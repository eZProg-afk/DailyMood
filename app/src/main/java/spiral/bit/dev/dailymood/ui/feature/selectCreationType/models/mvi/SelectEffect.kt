package spiral.bit.dev.dailymood.ui.feature.selectCreationType.models.mvi

sealed class SelectEffect {
    object NavigateToCreateManually : SelectEffect()
    object NavigateToCreateVoice : SelectEffect()
    object NavigateToCreateSurvey : SelectEffect()
    object NavigateToCreatePhoto : SelectEffect()
    object NavigateBack : SelectEffect()
}