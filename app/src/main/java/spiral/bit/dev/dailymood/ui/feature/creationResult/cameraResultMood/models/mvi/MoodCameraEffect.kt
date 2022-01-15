package spiral.bit.dev.dailymood.ui.feature.creationResult.cameraResultMood.models.mvi

sealed class MoodCameraEffect {
    object NavigateToCameraAddMood : MoodCameraEffect()
    object NavigateToMain : MoodCameraEffect()
}