package spiral.bit.dev.dailymood.ui.feature.detail.models.mvi

import spiral.bit.dev.dailymood.data.emotion.EmotionItem
import spiral.bit.dev.dailymood.ui.base.StateMarker

data class DetailState(
    val emotionItem: EmotionItem
) : StateMarker