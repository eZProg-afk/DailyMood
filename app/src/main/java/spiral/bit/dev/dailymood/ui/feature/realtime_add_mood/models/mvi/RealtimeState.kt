package spiral.bit.dev.dailymood.ui.feature.realtime_add_mood.models.mvi

import spiral.bit.dev.dailymood.ui.base.StateMarker

data class RealtimeState(
    val smileProbability: Float
) : StateMarker