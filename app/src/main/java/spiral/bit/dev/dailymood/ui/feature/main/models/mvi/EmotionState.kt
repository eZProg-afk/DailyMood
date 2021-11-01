package spiral.bit.dev.dailymood.ui.feature.main.models.mvi

import spiral.bit.dev.dailymood.ui.base.StateMarker
import spiral.bit.dev.dailymood.ui.common.adapter.models.ListItem

data class EmotionState(
    val moodEntities: List<ListItem>
) : StateMarker