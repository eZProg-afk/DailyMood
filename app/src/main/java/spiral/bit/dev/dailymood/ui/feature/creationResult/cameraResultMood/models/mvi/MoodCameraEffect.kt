package spiral.bit.dev.dailymood.ui.feature.creationResult.cameraResultMood.models.mvi

import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class MoodCameraEffect : SideEffectMarker {
    object NavigateToCameraAddMood : MoodCameraEffect()
    object NavigateToMain : MoodCameraEffect()
}