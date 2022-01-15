package spiral.bit.dev.dailymood.ui.feature.creationMood.voiceAddMood.models.mvi

sealed class VoiceEffect {
    object NavigateToMain : VoiceEffect()
    object NavigateBack : VoiceEffect()
    object TooQuiet : VoiceEffect()
}