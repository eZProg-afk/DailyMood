package spiral.bit.dev.dailymood.ui.feature.camera_result_mood.models.mvi

import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class MoodCameraEffect : SideEffectMarker {
    object NavigateToCameraAddMood : MoodCameraEffect()
    object NavigateToMain : MoodCameraEffect()
}