package spiral.bit.dev.dailymood.ui.feature.add_emotion_types.voice_add_emotion.models.mvi

import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class VoiceEffect : SideEffectMarker {
    object NavigateToMain : VoiceEffect()
    object TooQuiet : VoiceEffect()
}