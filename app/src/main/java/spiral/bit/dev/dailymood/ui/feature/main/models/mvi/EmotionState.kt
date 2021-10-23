package spiral.bit.dev.dailymood.ui.feature.main.models.mvi

import spiral.bit.dev.dailymood.data.emotion.MoodEntity
import spiral.bit.dev.dailymood.ui.base.StateMarker

data class EmotionState(
    val moodEntities: List<MoodEntity>
) : StateMarker