package spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.realtime.models.mvi

import spiral.bit.dev.dailymood.ui.base.StateMarker

data class RealtimeState(
    val smileProbability: Float
) : StateMarker