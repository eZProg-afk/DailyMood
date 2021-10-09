package spiral.bit.dev.dailymood.ui.feature.main.models.common

import spiral.bit.dev.dailymood.data.Emotion
import spiral.bit.dev.dailymood.ui.base.StateMarker

data class EmotionState(
    val emotions: List<Emotion>
) : StateMarker