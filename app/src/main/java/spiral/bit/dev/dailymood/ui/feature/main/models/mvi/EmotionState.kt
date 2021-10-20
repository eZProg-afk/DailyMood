package spiral.bit.dev.dailymood.ui.feature.main.models.mvi

import spiral.bit.dev.dailymood.data.emotion.EmotionItem
import spiral.bit.dev.dailymood.ui.base.StateMarker

data class EmotionState(
    val emotionItems: List<EmotionItem>
) : StateMarker