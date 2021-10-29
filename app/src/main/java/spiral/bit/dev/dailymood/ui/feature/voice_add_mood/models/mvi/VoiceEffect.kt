package spiral.bit.dev.dailymood.ui.feature.voice_add_mood.models.mvi

import spiral.bit.dev.dailymood.ui.base.SideEffectMarker

sealed class VoiceEffect : SideEffectMarker {
    object NavigateToMain : VoiceEffect()
    object TooQuiet : VoiceEffect()
}